package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.service;

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
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinHistory;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinUsageType;
import ticketaka.mtvs3_final_backend.coin.query.repository.CoinHistoryQueryRepository;
import ticketaka.mtvs3_final_backend.mail.command.application.service.MailCommandService;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.member.command.application.service.MemberCommandService;
import ticketaka.mtvs3_final_backend.redis.seat.postpone.domain.SeatPostpone;
import ticketaka.mtvs3_final_backend.redis.seat.postpone.repository.SeatPostponeRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.PaymentStatus;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository.MemberSeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.application.dto.SeatCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.repository.MemberSeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.service.TicketCommandService;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SeatCommandService {

    private final MemberCommandService memberCommandService;
    private final SeatReceptionService seatReceptionService;
    private final SeatDrawingService seatDrawingService;
    private final TicketCommandService ticketCommandService;
    private final CoinHistoryService coinHistoryService;
    private final MailCommandService mailCommandService;

    private final ConcertQueryRepository concertQueryRepository;
    private final SeatCommandRepository seatCommandRepository;
    private final MemberSeatCommandRepository memberSeatCommandRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;
    private final SeatPostponeRedisRepository seatPostponeRedisRepository;
    private final MemberSeatQueryRepository memberSeatQueryRepository;
    private final CoinHistoryQueryRepository coinHistoryQueryRepository;

    /*
        좌석 접수
     */
    public SeatCommandResponseDTO.seatReceptionDTO seatReception(Long memberId, Long concertId, Long seatId) {

        // Member 조회
        Member member = getMember(memberId);
        // Concert 조회
        Concert concert = getReservingConcert(concertId);

        // 좌석 접수
        Seat seat = seatReceptionService.seatReception(memberId, concertId, seatId);

        // 좌석 접수 우편 전송
        mailCommandService.mailForSeatReception(member.getId(), member.getMemberInfo().getNickname(), concert.getName(), formatSeatInfo(seat));

        return new SeatCommandResponseDTO.seatReceptionDTO(
                seat.getPrice(),
                getCompetitionRate(getReceptionMemberCount(concertId, seatId)),
                concert.getReceptionLimit() - getReceptionCountForConcert(memberId, concertId),
                true
        );
    }

    /*
        좌석 접수 취소
     */
    public SeatCommandResponseDTO.cancelReceptionSeatDTO cancelReception(Long memberId, Long concertId, Long seatId) {

        // Member 조회
        Member member = getMember(memberId);
        // Concert 조회
        Concert concert = getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        // 좌석 접수 취소
        seatReceptionService.cancelReception(memberId, concertId, seatId);

        // 우편 전송
        mailCommandService.mailForCancelSeatReception(memberId, member.getMemberInfo().getNickname(), concert.getName(), formatSeatInfo(seat));
        
        return new SeatCommandResponseDTO.cancelReceptionSeatDTO(
                concert.getReceptionLimit() - getReceptionCountForConcert(memberId, concertId),
                false
        );
    }

    /*
        추첨 시작 알림
     */
    public SeatCommandResponseDTO.createDrawingNotificationDTO drawingNotification(Long memberId, Long concertId, Long seatId) {

        // Concert 조회
        getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        // 추첨 시작 알림
        List<String> nicknameList = seatDrawingService.drawingNotification(concertId, seatId);

        return new SeatCommandResponseDTO.createDrawingNotificationDTO(
                nicknameList,
                getCompetitionRate(nicknameList.size()),
                formatSeatInfo(seat)
        );
    }

    /*
        좌석 추첨 결과 반영
     */
    public void processDrawResult(Long memberId, Long concertId, Long seatId) {

        // Member 조회
        getMember(memberId);
        // Concert 조회
        getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);
        // MemberSeat 조회
        MemberSeat memberSeat = getMemberSeat(memberId, concertId, seatId, MemberSeatStatus.RECEIVED);

        // 임시 결제 권한 획득
        newDrawResult(memberId, concertId, seatId);

        seat.setSeatStatus(SeatStatus.RESERVED);
        memberSeat.setMemberSeatStatus(MemberSeatStatus.WAITING_RESERVE);
        seatCommandRepository.save(seat);
        memberSeatCommandRepository.save(memberSeat);

        // 당첨되지 않은 다른 인원들 MemberSeatStatus - FAILED
        memberSeatCommandRepository.updateStatusToFailedForOthers(memberId, concertId, seatId);
    }

    /*
        좌석 결제 연기
     */
    public void postponeSeat(Long memberId, Long concertId, Long seatId) {

        // Member 조회
        Member member = getMember(memberId);
        // Concert 조회
        Concert concert = getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        MemberSeat memberSeat = getMemberSeat(memberId, concertId, seatId, MemberSeatStatus.WAITING_RESERVE);
        memberSeat.setMemberSeatStatus(MemberSeatStatus.POSTPONE);
        memberSeatCommandRepository.save(memberSeat);

        Mail mail = mailCommandService.mailForPostponeSeatReservation(member.getId(), member.getMemberInfo().getNickname(), concert.getName(), formatSeatInfo(seat));

        SeatPostpone seatPostpone = newSeatPostpone(mail.getId(), memberId, concertId, seatId);
        seatPostponeRedisRepository.save(seatPostpone);
    }

    /*
        좌석 결제
     */
    public SeatCommandResponseDTO.reserveSeatDTO reserveSeat(Long memberId, Long concertId, Long seatId) {

        // Member 확인
        Member member = getMember(memberId);
        // Concert 조회
        Concert concert = getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        // 좌석 결제 권한 확인
        validateDrawResult(getDrawResult(memberId, concertId, seatId));
        // 좌석 결제
        calculateCoin(member, seat);

        // Seat 예약
        reserveMemberSeat(member, concert, seat);

        String seatInfo = formatSeatInfo(seat);

        // Ticket 생성
        Long ticketId = ticketCommandService.createTicket(memberId, concertId, seatId).ticketId();

        // 주소지 티켓 매핑
        Address address = memberCommandService.saveTicketAddress(memberId, concertId, seatId, ticketId);

        // SMS 문자 보내기
        memberCommandService.sendReserveSMS(address.getPhoneNumber(), concert.getName(), seatInfo, concert.getConcertDate());

        // Mail 발송
        mailCommandService.mailForSeatReservation(member.getId(), member.getMemberInfo().getNickname(), concert.getName(), formatSeatInfo(seat));

        // TODO: seatNum
        return new SeatCommandResponseDTO.reserveSeatDTO(
                seat.getId().intValue(),
                formatSeatName(concert, seat),
                seatInfo,
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                formatUserAddress(address),
                ticketId.intValue()
        );
    }

    /*
        좌석 결제 취소
     */
    public void cancelReserveSeat(Long memberId, Long concertId, Long seatId) {

        Member member = getMember(memberId);
        Concert concert = getReservingConcert(concertId);
        Seat seat = getSeat(seatId);

        MemberSeat memberSeat = memberSeatQueryRepository.findByMemberIdAndConcertIdAndSeatId(memberId, concertId, seatId)
                .orElseThrow(() -> new Exception403("해당 좌석에 대한 권한이 없습니다."));

        validateReserveSeat(memberSeat.getMemberSeatStatus());

        // 취소 처리
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        memberSeat.setMemberSeatStatus(MemberSeatStatus.CANCEL_RESERVE);

        seatCommandRepository.save(seat);
        memberSeatCommandRepository.save(memberSeat);

        // Coin 환불
        refundReserveSeat(member, seatId);
    }

    /*
        추첨 결과 치트
     */
    public void cheatDrawResult(Long memberId, Long concertId) {

        Member member = getMember(memberId);
        Concert concert = getReservingConcert(concertId);

        MemberSeat memberSeat = memberSeatCommandRepository.findFirstByMemberIdAndConcertIdAndMemberSeatStatus(member.getId(), concert.getId(), MemberSeatStatus.FAILED)
                .orElseThrow(() -> new Exception400("해당 콘서트에 접수한 좌석이 없습니다."));
        memberSeat.setMemberSeatStatus(MemberSeatStatus.WAITING_RESERVE);
        memberSeatCommandRepository.save(memberSeat);

        newDrawResult(memberId, concert.getId(), memberSeat.getSeatId());
    }

    /*
        좌석 결제 - 치트
     */
    public SeatCommandResponseDTO.reserveSeatDTO cheatReserveSeat(Long memberId, Long concertId) {

        // Member 확인
        Member member = getMember(memberId);

        // Concert & Seat 조회
        Concert concert = getReservingConcert(concertId);
        MemberSeat memberSeat = memberSeatCommandRepository.findFirstByMemberIdAndConcertIdAndMemberSeatStatus(member.getId(), concert.getId(), MemberSeatStatus.RECEIVED)
                .orElseThrow(() -> new Exception400("해당 콘서트에 접수한 좌석이 없습니다."));

        Seat seat = seatCommandRepository.findById(memberSeat.getSeatId())
                .orElseThrow(() -> new Exception400("해당 좌석을 찾을 수 업습니다."));

        seat.setSeatStatus(SeatStatus.RESERVED);
        seatCommandRepository.save(seat);

        // 배송지 정보 조회
        Address address = getAddress(member);
        
        Long ticketId = ticketCommandService.createTicket(memberId, concertId, seat.getId()).ticketId();

        return new SeatCommandResponseDTO.reserveSeatDTO(
                seat.getId().intValue(),
                concert.getConcertDate().getYear() + formatSeatInfo(seat),
                formatSeatInfo(seat),
                1,
                seat.getPrice(),
                member.getCoin(),
                address.getUserName(),
                address.getPhoneNumber(),
                formatUserAddress(address),
                ticketId.intValue()
        );
    }

    // Member 조회
    private Member getMember(Long currentMemberId) {
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getReservingConcert(Long concertId) {
        return concertQueryRepository.findByIdAndConcertStatus(concertId, ConcertStatus.RESERVING)
                .orElseThrow(() -> new Exception400("해당 콘서트는 현재 예약 가능한 상태가 아닙니다."));
    }

    // Seat 조회
    private Seat getSeat(Long seatId) {
        return seatCommandRepository.findById(seatId)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // 최근 입력한 Address 조회
    private Address getAddress(Member member) {
        return addressRepository.findFirstByMemberIdOrderByCreatedAtDesc(member.getId())
                .orElseThrow(() -> new Exception400("배송지 정보를 조회할 수 없습니다."));
    }

    // MemberSeat 조회
    private MemberSeat getMemberSeat(Long memberId, Long concertId, Long seatId, MemberSeatStatus memberSeatStatus) {
        return memberSeatCommandRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(memberId, concertId, seatId, memberSeatStatus)
                .orElseThrow(() -> new Exception400("해당 좌석을 접수한 내역을 찾을 수 없습니다."));
    }

    // DrawResult 조회
    private DrawResult getDrawResult(Long memberId, Long concertId, Long seatId) {
        String id = memberId + "-" + concertId + "-" + seatId;
        return drawResultRedisRepository.findById(id)
                .orElseThrow(() -> new Exception403("좌석 결제 권한이 없습니다."));
    }

    // DrawResult 생성
    private void newDrawResult(Long memberId, Long concertId, Long seatId) {
        DrawResult drawResult = DrawResult.builder()
                .memberId(memberId)
                .concertId(concertId)
                .seatId(seatId)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        drawResultRedisRepository.save(drawResult);
    }

    // SeatPostpone 생성
    private SeatPostpone newSeatPostpone(Long mailId, Long memberId, Long concertId, Long seatId) {
        return SeatPostpone.builder()
                .id(mailId.toString())
                .memberId(memberId)
                .concertId(concertId)
                .seatId(seatId)
                .build();
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

    // Member 가 해당 Concert 에 접수한 좌석 수 조회
    private int getReceptionCountForConcert(Long currentMemberId, Long concertId) {
        return memberSeatCommandRepository.countByMemberIdAndConcertId(currentMemberId, concertId);
    }

    // 해당 좌석에 접수한 회원 수 조회
    private int getReceptionMemberCount(Long concertId, Long seatId) {
        return memberSeatCommandRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, MemberSeatStatus.RECEIVED
        );
    }

    // 경쟁률 계산
    private int getCompetitionRate(int receptionMemberCount) {
        double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;
        return (int) Math.round(competitionRate);
    }

    // MemberSeatStatus 유효성 검사
    private void validateReserveSeat(MemberSeatStatus memberSeatStatus) {
        switch (memberSeatStatus) {
            case FAILED -> throw new Exception400("추첨에 떨어진 좌석입니다.");
            case RECEIVED -> throw new Exception400("결제 권한을 얻지 못한 좌석입니다.");
            case CANCEL_RESERVE -> throw new Exception400("예약을 취소한 좌석입니다.");
        }
    }

    // DrawResult 유효성 검사
    private void validateDrawResult(DrawResult drawResult) {
        switch (drawResult.getPaymentStatus()) {
            case PENDING -> throw new Exception400("배송지 입력이 되지 않았습니다.");
            case FAILED -> throw new Exception400("좌석 추첨 결과가 유효하지 않습니다.");
        }
        drawResultRedisRepository.delete(drawResult);
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
                CoinUsageType.USAGE,
                seat.getPrice()
        ));
    }

    // 예약 좌석 환불
    private void refundReserveSeat(Member member, Long seatId) {

        CoinHistory coinHistory = coinHistoryQueryRepository.findByMemberIdAndAcquisitionTypeAndCoinAcquisitionId(
                member.getId(), AcquisitionType.SEAT_RESERVATION, seatId
        ).orElseThrow(() -> new Exception400("해당 결제 내역을 찾을 수 없습니다."));

        Integer seatPrice = coinHistory.getAmount();
        member.setCoin(member.getCoin() + seatPrice);
        memberRepository.save(member);

        coinHistoryService.saveCoinHistory(new CoinHistoryRequestDTO.saveCoinHistoryDTO(
                member.getId(),
                AcquisitionType.SEAT_RESERVATION,
                seatId,
                CoinUsageType.CANCEL,
                seatPrice
        ));
    }

    // MemberSeat 예약 상태 전환
    private void reserveMemberSeat(Member member, Concert concert, Seat seat) {
        MemberSeat memberSeat = getReserveMemberSeat(member.getId(), concert.getId(), seat.getId());
        memberSeat.setMemberSeatStatus(MemberSeatStatus.RESERVED);
        memberSeatCommandRepository.save(memberSeat);
    }

    // MemberSeat 조회
    private MemberSeat getReserveMemberSeat(Long memberId, Long concertId, Long seatId) {
        return memberSeatCommandRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatusIn(
                        memberId, concertId, seatId, List.of(MemberSeatStatus.WAITING_RESERVE, MemberSeatStatus.POSTPONE))
                .orElseThrow(() -> new Exception403("해당 좌석을 결제할 권한이 없습니다."));
    }
}