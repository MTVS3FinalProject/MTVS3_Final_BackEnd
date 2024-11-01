package ticketaka.mtvs3_final_backend.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinHistoryRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.application.service.CoinHistoryService;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.AcquisitionType;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinUsageType;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.PaymentStatus;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.MemberSeatRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatService {

    private final CoinHistoryService coinHistoryService;

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final MemberSeatRepository memberSeatRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;

    /*
        좌석 접수
     */
    @Transactional
    public SeatResponseDTO.seatReceptionDTO seatReception(Long concertId, Long seatId, Long currentMemberId) {

        // Member 조회
        getMember(currentMemberId);
        // Concert 조회
        Concert concert = getConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        // 좌석 접수 유효성 확인
        checkAvailableSeat(concertId, seatId, currentMemberId);
        // 좌석 접수
        receiptSeat(currentMemberId, concertId, seatId);

        return new SeatResponseDTO.seatReceptionDTO(
                seat.getPrice(),
                getCompetitionRate(getReceptionMemberCount(concertId, seatId)),
                concert.getReceptionLimit() - getReceptionCountForConcert(currentMemberId, concertId)
        );
    }

    /*
        현재 회원이 접수한 모든 좌석 조회
    */
    public SeatResponseDTO.getReceptionSeatsDTO getReceptionSeats(Long concertId, Long currentMemberId) {

        // Member 조회
        getMember(currentMemberId);
        // Concert
        Concert concert = getConcert(concertId);

        // 현재 회원이 접수한 좌석 목록 조회
        List<Seat> receptionSeatList = getReceptionSeatsForConcert(currentMemberId, concert);
        // 최종 DTO 생성 및 반환
        return new SeatResponseDTO.getReceptionSeatsDTO(getReceptionSeatsDTO(concert, receptionSeatList));
    }

    /*
        좌석 접수 취소
     */
    @Transactional
    public SeatResponseDTO.cancelReceptionSeatDTO cancelReceptionSeat(Long concertId, Long seatId, Long currentMemberId) {

        // Member 조회
        getMember(currentMemberId);
        // Concert 조회
        Concert concert = getConcert(concertId);
        // Seat 조회
        getSeat(seatId);

        // 좌석 접수 취소
        cancelMemberSeat(currentMemberId, concertId, seatId);

        return new SeatResponseDTO.cancelReceptionSeatDTO(
                concert.getReceptionLimit() - getReceptionCountForConcert(currentMemberId, concertId)
        );
    }

    /*
        추첨 시작 알림
     */
    public SeatResponseDTO.createDrawingNotificationDTO createDrawingNotification(Long concertId, Long seatId) {

        // Concert 조회
        getConcert(concertId);
        // Seat 조회
        getSeat(seatId);

        List<String> nicknameList = getMembersForDrawing(concertId, seatId, MemberSeatStatus.RECEIVED).stream()
                .map(Member::getNickname)
                .toList();

        return new SeatResponseDTO.createDrawingNotificationDTO(
                nicknameList,
                getCompetitionRate(nicknameList.size())
        );
    }

    /*
        좌석 추첨 결과 반영
     */
    @Transactional
    public void processDrawResult(Long concertId, Long seatId, Long currentMemberId) {

        // Member 조회
        getMember(currentMemberId);
        // Concert 조회
        getConcert(concertId);
        // Seat 조회
        getSeat(seatId);
        // MemberSeat 조회
        MemberSeat memberSeat = getMemberSeat(currentMemberId, concertId, seatId, MemberSeatStatus.RECEIVED);

        // 임시 결제 권한 획득
        newDrawResult(currentMemberId, concertId, seatId);

        memberSeat.setMemberSeatStatus(MemberSeatStatus.WAITING_RESERVE);
        memberSeatRepository.save(memberSeat);
    }

    /*
        좌석 결제
     */
    @Transactional
    public SeatResponseDTO.reserveSeatDTO reserveSeat(Long concertId, Long seatId, Long currentMemberId) {

        // Member 확인
        Member member = getMember(currentMemberId);
        // 배송지 정보 조회
        Address address = getAddress(member);
        // Concert 조회
        Concert concert = getConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        // 좌석 결제 권한 확인
        validateDrawResult(getDrawResult(member));
        // 좌석 결제
        calculateCoin(member, seat);

        // Seat 예약
        reserveMemberSeat(member, concert, seat);

        seat.setSeatStatus(SeatStatus.RESERVED);
        seatRepository.save(seat);

        // TODO: 티켓 생성, seatNum
        return new SeatResponseDTO.reserveSeatDTO(
                seat.getId().intValue(),
                formatSeatName(concert, seat),
                formatSeatInfo(seat),
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                formatUserAddress(address)
        );
    }

    /*
        추첨 결과 치트
     */
    public void cheatDrawResult(Long concertId, Long currentMemberId) {

        Member member = getMember(currentMemberId);
        Concert concert = getConcert(concertId);

        MemberSeat memberSeat = memberSeatRepository.findFirstByMemberIdAndConcertIdAndMemberSeatStatus(member.getId(), concert.getId(), MemberSeatStatus.RECEIVED)
                .orElseThrow(() -> new Exception400("해당 콘서트에 접수한 좌석이 없습니다."));

        newDrawResult(currentMemberId, concert.getId(), memberSeat.getSeatId());
    }

    /*
        좌석 결제 - 치트
     */
    public SeatResponseDTO.reserveSeatDTO cheatReserveSeat(Long concertId, Long currentMemberId) {

        // Member 확인
        Member member = getMember(currentMemberId);

        // Concert & Seat 조회
        Concert concert = getConcert(concertId);
        MemberSeat memberSeat = memberSeatRepository.findFirstByMemberIdAndConcertIdAndMemberSeatStatus(member.getId(), concert.getId(), MemberSeatStatus.RECEIVED)
                .orElseThrow(() -> new Exception400("해당 콘서트에 접수한 좌석이 없습니다."));

        Seat seat = seatRepository.findById(memberSeat.getSeatId())
                .orElseThrow(() -> new Exception400("해당 좌석을 찾을 수 업습니다."));

        seat.setSeatStatus(SeatStatus.RESERVED);
        seatRepository.save(seat);

        // 배송지 정보 조회
        Address address = getAddress(member);

        // TODO: seatNum
        return new SeatResponseDTO.reserveSeatDTO(
                seat.getId().intValue(),
                concert.getConcertDate().getYear() + formatSeatInfo(seat),
                formatSeatInfo(seat),
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                formatUserAddress(address)
        );
    }

    // Member 조회
    private Member getMember(Long currentMemberId) {
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    // Seat 조회
    private Seat getSeat(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // 최근 입력한 Address 조회
    private Address getAddress(Member member) {
        return addressRepository.findFirstByMemberIdOrderByCreatedAtDesc(member.getId())
                .orElseThrow(() -> new Exception400("배송지 정보를 조회할 수 없습니다."));
    }

    // MemberSeat 조회
    private MemberSeat getMemberSeat(Long currentMemberId, Long concertId, Long seatId, MemberSeatStatus memberSeatStatus) {
        return memberSeatRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(currentMemberId, concertId, seatId, memberSeatStatus)
                .orElseThrow(() -> new Exception400("해당 좌석을 접수한 내역을 찾을 수 없습니다."));
    }

    // DrawResult 조회
    private DrawResult getDrawResult(Member member) {
        return drawResultRedisRepository.findById(String.valueOf(member.getId()))
                .orElseThrow(() -> new Exception403("좌석 결제 권한이 없습니다."));
    }

    // MemberSeat 생성
    private MemberSeat newMemberSeat(Long currentMemberId, Long concertId, Long seatId) {
        return MemberSeat.builder()
                .memberId(currentMemberId)
                .concertId(concertId)
                .seatId(seatId)
                .memberSeatStatus(MemberSeatStatus.RECEIVED)
                .build();
    }

    // DrawResult 생성
    private void newDrawResult(Long currentMemberId, Long concertId, Long seatId) {
        DrawResult drawResult = DrawResult.builder()
                .id(String.valueOf(currentMemberId))
                .concertId(concertId)
                .seatId(seatId)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        drawResultRedisRepository.save(drawResult);
    }

    // SeatName 생성
    private String formatSeatName(Concert concert, Seat seat) {
        return concert.getConcertDate().getYear() + seat.getSection() + seat.getNumber();
    }

    // SeatInfo 생성
    private String formatSeatInfo(Seat seat) {
        return seat.getSection() + "구역 " + seat.getNumber() + "번";
    }

    // UserAddress 생성
    private String formatUserAddress(Address address) {
        return address.getAddress() + " " + address.getDetail();
    }

    // TimeDTO 생성
    private SeatResponseDTO.timeDTO getTimeDTO(LocalDateTime localDateTime) {
        return new SeatResponseDTO.timeDTO(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.toLocalTime().toString()
        );
    }

    // 해당 Concert & Seat 에 접수한 회원 목록 조회
    private List<Member> getMembersForDrawing(Long concertId, Long seatId, MemberSeatStatus status) {
        return memberRepository.findByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, status
        );
    }

    // Member 가 해당 Concert 에서 접수한 Seat 목록 조회
    private List<Seat> getReceptionSeatsForConcert(Long currentMemberId, Concert concert) {
        return seatRepository.findAllSeatsByMemberIdAndConcertIdAndStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );
    }

    // 해당 공연에 접수한 좌석 리스트 -> getReceptionSeatsDTO
    private List<SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO> getReceptionSeatsDTO(Concert concert, List<Seat> receptionSeatList) {
        return receptionSeatList.stream()
                .map(seat -> new SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO(
                        seat.getId().intValue(),
                        formatSeatName(concert, seat),
                        formatSeatInfo(seat),
                        getTimeDTO(concert.getConcertDate()),
                        getTimeDTO(seat.getDrawingTime()),
                        getCompetitionRate(getReceptionMemberCount(concert.getId(), seat.getId()))
                ))
                .toList();
    }

    // Member 가 해당 Concert 에 접수한 좌석 수 조회
    private int getReceptionCountForConcert(Long currentMemberId, Long concertId) {
        return memberSeatRepository.countByMemberIdAndConcertId(currentMemberId, concertId);
    }

    // 좌석 접수
    private void receiptSeat(Long currentMemberId, Long concertId, Long seatId) {
        MemberSeat memberSeat = newMemberSeat(currentMemberId, concertId, seatId);
        memberSeatRepository.save(memberSeat);
    }

    // 좌석 접수 취소
    private void cancelMemberSeat(Long currentMemberId, Long concertId, Long seatId) {
        MemberSeat memberSeat = getMemberSeat(currentMemberId, concertId, seatId, MemberSeatStatus.RECEIVED);
        memberSeatRepository.delete(memberSeat);
    }

    // 해당 좌석에 접수한 회원 수 조회
    private int getReceptionMemberCount(Long concertId, Long seatId) {
        return memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, MemberSeatStatus.RECEIVED
        );
    }

    // 경쟁률 계산
    private int getCompetitionRate(int receptionMemberCount) {
        double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;
        return (int) Math.round(competitionRate);
    }

    // DrawResult 유효성 검사
    private void validateDrawResult(DrawResult drawResult) {
        switch (drawResult.getPaymentStatus()) {
            case PENDING -> throw new Exception400("배송지 입력이 되지 않았습니다.");
            case FAILED -> throw new Exception400("좌석 추첨 결과가 유효하지 않습니다.");
        }
        drawResultRedisRepository.delete(drawResult);
    }

    // 좌석 접수 유효성 확인
    private void checkAvailableSeat(Long concertId, Long seatId, Long currentMemberId) {
        // 이미 예약된 좌석인지 확인
        checkAlreadyReserved(concertId, seatId);
        // 이미 접수된 좌석인지 확인
        checkAlreadyReceipted(currentMemberId, concertId, seatId);
    }

    // 이미 예약된 Seat 인지 검사
    private void checkAlreadyReserved(Long concertId, Long seatId) {
        seatRepository.findByConcertIdAndIdAndSeatStatus(concertId, seatId, SeatStatus.RESERVED)
                .ifPresent(seat -> {
                    throw new Exception400("이미 예약된 좌석입니다.");
                });
    }

    // 이미 접수된 Seat 인지 검사
    private void checkAlreadyReceipted(Long currentMemberId, Long concertId, Long seatId) {
        memberSeatRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(
                currentMemberId, concertId, seatId, MemberSeatStatus.RECEIVED
                ).ifPresent(memberSeat -> {
                    throw new Exception400("이미 접수한 좌석입니다.");
                });
    }

    // Coin 계산 - Seat 예약
    private void calculateCoin(Member member, Seat seat) {
        if(member.getCoin() < seat.getPrice()) {
            throw new Exception400("코인이 부족합니다.");
        }

        member.setCoin(member.getCoin()- seat.getPrice());
        memberRepository.save(member);

        coinHistoryService.saveCoinHistory(new CoinHistoryRequestDTO.saveCoinHistoryDTO(
                member.getId(),
                AcquisitionType.SEAT_RESERVATION,
                seat.getId(),
                CoinUsageType.USAGE
        ));
    }

    // MemberSeat 예약 상태 전환
    private void reserveMemberSeat(Member member, Concert concert, Seat seat) {
        MemberSeat memberSeat = getMemberSeat(member.getId(), concert.getId(), seat.getId(), MemberSeatStatus.WAITING_RESERVE);
        memberSeat.setMemberSeatStatus(MemberSeatStatus.RESERVED);
        memberSeatRepository.save(memberSeat);
    }
}
