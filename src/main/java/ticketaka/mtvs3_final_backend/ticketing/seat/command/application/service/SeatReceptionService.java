package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.application.service.MemberSeatCommandService;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository.MemberSeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatReceptionService {

    private final MemberSeatCommandService memberSeatCommandService;

    private final MemberSeatQueryRepository memberSeatQueryRepository;
    private final SeatQueryRepository seatQueryRepository;

    /*
        좌석 접수
     */
    @Transactional
    public Seat seatReception(Long memberId, Long concertId, Long seatId) {

        // 접수 가능한 Seat 조회
        Seat seat = getAvailableSeat(concertId, seatId);
        // MemberSeat 상태 확인
        checkAlreadyReceipted(memberId, concertId, seatId);

        // 좌석 접수
        memberSeatCommandService.seatReception(memberId, concertId, seatId);

        return seat;
    }

    // 접수 가능한 Seat 조회
    private Seat getAvailableSeat(Long concertId, Long seatId) {
        return seatQueryRepository.findByConcertIdAndIdAndSeatStatus(
                concertId, seatId, SeatStatus.AVAILABLE
        ).orElseThrow(() -> new Exception400("접수 불가능한 좌석입니다."));
    }

    // MemberSeat 상태 확인
    private void checkAlreadyReceipted(Long memberId, Long concertId, Long seatId) {
        memberSeatQueryRepository.findByMemberIdAndConcertIdAndSeatId(
                memberId, concertId, seatId
        ).ifPresent(memberSeat -> {
            throw new Exception400("이미 접수하신 좌석입니다.");
        });
    }
}
