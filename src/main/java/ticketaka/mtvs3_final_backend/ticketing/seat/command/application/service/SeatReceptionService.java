package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository.MemberSeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatReceptionService {

    private final SeatCommandRepository seatCommandRepository;
    private final MemberSeatQueryRepository memberSeatQueryRepository;

    /*
        좌석 접수
     */
    @Transactional
    public void seatReception(
            Long memberId, Long concertId, Long seatId) {

        // 좌석 접수 유효성 확인
        checkAlreadyReserved(concertId, seatId);
        // 이미 접수된 좌석인지 확인
        checkAlreadyReceipted(memberId, concertId, seatId);

        // 좌석 접수
        receiptSeat(memberId, concertId, seatId);
    }

    // 이미 예약된 Seat 인지 검사
    private void checkAlreadyReserved(Long concertId, Long seatId) {
        seatCommandRepository.findByConcertIdAndIdAndSeatStatus(concertId, seatId, SeatStatus.RESERVED)
                .ifPresent(seat -> {
                    throw new Exception400("이미 예약된 좌석입니다.");
                });
    }

    // 이미 접수된 Seat 인지 검사
    private void checkAlreadyReceipted(Long currentMemberId, Long concertId, Long seatId) {
        memberSeatQueryRepository.findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(
                currentMemberId, concertId, seatId, MemberSeatStatus.RECEIVED
        ).ifPresent(memberSeat -> {
            throw new Exception400("이미 접수한 좌석입니다.");
        });
    }

    // 좌석 접수
    private void receiptSeat(Long currentMemberId, Long concertId, Long seatId) {
        MemberSeat memberSeat = newMemberSeat(currentMemberId, concertId, seatId);
        memberSeatRepository.save(memberSeat);
    }
}
