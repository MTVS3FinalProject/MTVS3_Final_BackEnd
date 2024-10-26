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
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
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
        좌석 조회
     */
    public SeatResponseDTO.getSeatDTO getSeat(SeatRequestDTO.seatIdDTO requestDTO) {

        Concert concert = getConcertByConcertName(requestDTO.concertName());
        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());
        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        return new SeatResponseDTO.getSeatDTO(
                requestDTO.seatId(),
                seat.getFloor(),
                formatSeatInfo(seat),
                getTimeDTO(concert.getConcertDate()),
                getTimeDTO(seat.getDrawingTime()),
                getCompetitionRate(getReceptionMemberCount(concert, seat))
        );
    }

    /*
        좌석 접수
     */
    @Transactional
    public SeatResponseDTO.seatReceptionDTO seatReception(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        Concert concert = getConcertByConcertName(requestDTO.concertName());
        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());
        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        checkAlreadyReceipted(currentMemberId, concert, seat);

        MemberSeat memberSeat = newMemberSeat(currentMemberId, concert.getId(), seat.getId());
        memberSeatRepository.save(memberSeat);

        return new SeatResponseDTO.seatReceptionDTO(
                requestDTO.seatId(),
                seat.getPrice(),
                getCompetitionRate(getReceptionMemberCount(concert, seat)),
                concert.getReceptionLimit() - getReceptionCountForConcert(currentMemberId, concert)
        );
    }

    /*
        현재 회원이 접수한 모든 좌석 조회
    */
    public SeatResponseDTO.getReceptionSeatsDTO getReceptionSeats(SeatRequestDTO.getReceptionSeatsDTO requestDTO, Long currentMemberId) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        // 현재 회원이 접수한 좌석 목록 조회
        List<Seat> receptionSeatList = seatRepository.findAllSeatsByMemberIdAndConcertIdAndStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );

        // 좌석 정보를 DTO 로 변환
        List<SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO> receptionSeatsDTOList = receptionSeatList.stream()
                .map(seat -> new SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO(
                        formatSeatInfo(seat),
                        getTimeDTO(concert.getConcertDate()),
                        getTimeDTO(seat.getDrawingTime()),
                        getCompetitionRate(getReceptionMemberCount(concert, seat))
                ))
                .toList();

        // 최종 DTO 생성 및 반환
        return new SeatResponseDTO.getReceptionSeatsDTO(receptionSeatsDTOList);
    }

    /*
        좌석 접수 취소
     */
    @Transactional
    public SeatResponseDTO.cancelReceptionSeatDTO cancelReceptionSeat(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        System.out.println("seat = " + seat);

        MemberSeat memberSeat = getMemberSeat(currentMemberId, concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED);

        memberSeatRepository.delete(memberSeat);

        return new SeatResponseDTO.cancelReceptionSeatDTO(
                concert.getReceptionLimit() - getReceptionCountForConcert(currentMemberId, concert)
        );
    }

    /*
        추첨 시작 알림
     */
    public SeatResponseDTO.drawingNotificationDTO drawingNotification(SeatRequestDTO.seatIdDTO requestDTO) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        List<Member> memberList = memberRepository.findByConcertIdAndSeatId(
                concert.getId(), seat.getId(), MemberSeatStatus.RESERVED
        );

        List<String> nicknameList = memberList.stream()
                .map(Member::getNickname)
                .toList();

        return new SeatResponseDTO.drawingNotificationDTO(
                nicknameList,
                getCompetitionRate(nicknameList.size())
        );
    }

    /*
        좌석 추첨 결과 반영
     */
    @Transactional
    public void createDrawResult(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        MemberSeat memberSeat = getMemberSeat(currentMemberId, concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED);

        // 임시 결제 권한 획득
        DrawResult drawResult = DrawResult.builder()
                .id(String.valueOf(currentMemberId))
                .concertId(concert.getId())
                .seatId(seat.getId())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        drawResultRedisRepository.save(drawResult);

        memberSeat.setMemberSeatStatus(MemberSeatStatus.WAITING_RESERVE);
        memberSeatRepository.save(memberSeat);
    }

    /*
        추첨 결과 치트
     */
    public void cheatDrawResult(SeatRequestDTO.cheatDTO requestDTO, Long currentMemberId) {

        Member member = getMember(currentMemberId);
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        MemberSeat memberSeat = memberSeatRepository.findFirstByMemberIdAndConcertIdAndMemberSeatStatus(member.getId(), concert.getId(), MemberSeatStatus.RECEIVED)
                .orElseThrow(() -> new Exception400("해당 콘서트에 접수한 좌석이 없습니다."));

        DrawResult drawResult = DrawResult.builder()
                .id(String.valueOf(currentMemberId))
                .concertId(concert.getId())
                .seatId(memberSeat.getSeatId())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        drawResultRedisRepository.save(drawResult);
    }

    /*
        좌석 결제
     */
    @Transactional
    public SeatResponseDTO.reserveSeatDTO reserveSeat(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // Member 확인
        Member member = getMember(currentMemberId);

        // 좌석 결제 권한 확인
        DrawResult drawResult = drawResultRedisRepository.findById(String.valueOf(member.getId()))
                .orElseThrow(() -> new Exception403("좌석 결제 권한이 없습니다."));

        validateDrawResult(drawResult);

        // 배송지 정보 조회
        Address address = getAddress(member);

        // 좌석 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());
        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());
        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        String seatInfo = formatSeatInfo(seat);

        // 좌석 결제
        // TODO: 예약 정보 생성
        int coin = calculateCoin(member.getCoin(), seat);

        member.setCoin(coin);

        memberRepository.save(member);

        seat.setSeatStatus(SeatStatus.RESERVED);
        seatRepository.save(seat);

        MemberSeat memberSeat = getMemberSeat(member.getId(), concert.getId(), seat.getId(), MemberSeatStatus.WAITING_RESERVE);
        memberSeat.setMemberSeatStatus(MemberSeatStatus.RESERVED);

        // 티켓 생성

        drawResultRedisRepository.delete(drawResult);

        coinHistoryService.saveCoinHistory(new CoinHistoryRequestDTO.saveCoinHistoryDTO(
                member.getId(),
                AcquisitionType.SEAT_RESERVATION,
                seat.getId(),
                CoinUsageType.USAGE
        ));

        // TODO: seatNum
        return new SeatResponseDTO.reserveSeatDTO(
                requestDTO.seatId(),
                seatInfo,
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                address.getAddress() + " " + address.getDetail()
        );
    }

    private int calculateCoin(int memberCoin, Seat seat) {
        if(memberCoin < seat.getPrice()) {
            throw new Exception400("코인이 부족합니다.");
        }

        memberCoin -= seat.getPrice();
        return memberCoin;
    }

    /*
        좌석 결제 - 치트
     */
    public SeatResponseDTO.reserveSeatDTO cheatReserveSeat(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // Member 확인
        Member member = getMember(currentMemberId);

        // 배송지 정보 조회
        Address address = getAddress(member);

        // 좌석 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());
        SeatDTO.getSeatId seatId = getSeatInfo(requestDTO.seatId());
        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        String seatInfo = formatSeatInfo(seat);

        seat.setSeatStatus(SeatStatus.RESERVED);
        seatRepository.save(seat);

        // TODO: seatNum
        return new SeatResponseDTO.reserveSeatDTO(
                requestDTO.seatId(),
                seatInfo,
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                address.getAddress() + " " + address.getDetail()
        );
    }

    // Member 조회
    private Member getMember(Long currentMemberId) {
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // 최근 입력한 Address 조회
    private Address getAddress(Member member) {
        return addressRepository.findFirstByMemberIdOrderByCreatedAtDesc(member.getId())
                .orElseThrow(() -> new Exception400("배송지 정보를 조회할 수 없습니다."));
    }

    // Concert 조회 - 공연 이름
    private Concert getConcertByConcertName(String concertName) {
        return concertRepository.findByName(concertName)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    // Seat 조회 - 공연, 구역, 번호
    private Seat getSeat(Concert concert, String section, String number) {
        return seatRepository.findByConcertAndSectionAndNumber(concert, section, number)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // MemberSeat 조회
    private MemberSeat getMemberSeat(Long currentMemberId, Long concertId, Long seatId, MemberSeatStatus memberSeatStatus) {
        return memberSeatRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(currentMemberId, concertId, seatId, memberSeatStatus)
                .orElseThrow(() -> new Exception400("해당 좌석을 접수한 내역을 찾을 수 없습니다."));
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

    // 좌석 번호로 Seat 구역, 번호 조회
    private SeatDTO.getSeatId getSeatInfo(String seatId) {
        return new SeatDTO.getSeatId(
                seatId.substring(4, 6),
                seatId.substring(6)
        );
    }

    // SeatInfo 생성
    private String formatSeatInfo(Seat seat) {
        return seat.getSection() + "구역 " + seat.getNumber() + "번";
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

    // Member 가 해당 Concert 에 접수한 좌석 수 조회
    private int getReceptionCountForConcert(Long currentMemberId, Concert concert) {
        return memberSeatRepository.countByMemberIdAndConcertId(currentMemberId, concert.getId());
    }

    // 해당 좌석에 접수한 회원 수 조회
    private int getReceptionMemberCount(Concert concert, Seat seat) {
        return memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED
        ).intValue();
    }

    // 경쟁률 계산
    private int getCompetitionRate(int receptionMemberCount) {
        double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;
        return (int) Math.round(competitionRate);
    }

    // DrawResult 유효성 검사
    private void validateDrawResult(DrawResult drawResult) {

        PaymentStatus paymentStatus = drawResult.getPaymentStatus();

        switch (paymentStatus) {
            case PENDING -> throw new Exception400("배송지 입력이 되지 않았습니다.");
            case FAILED -> throw new Exception400("좌석 추첨 결과가 유효하지 않습니다.");
        }
    }

    // 이미 접수된 Seat 인지 검사
    private void checkAlreadyReceipted(Long currentMemberId, Concert concert, Seat seat) {
        memberSeatRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(currentMemberId, concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED)
                .ifPresent(memberSeat -> {
                    throw new Exception400("이미 접수한 좌석입니다.");
                });
    }
}
