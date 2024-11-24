package ticketaka.mtvs3_final_backend.ticketing.memberseat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.repository.MemberSeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository.MemberSeatQueryRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberSeatCommandService {

    private final MemberSeatCommandRepository memberSeatCommandRepository;
    private final MemberSeatQueryRepository memberSeatQueryRepository;

    /*
        좌석 접수
     */
    @Transactional
    public void seatReception(Long memberId, Long concertId, Long seatId) {

        // MemberSeat 조회
        if (getMemberSeatOrNull(memberId, concertId, seatId) != null) {
            throw new Exception400("이미 접수하신 좌석입니다.");
        };

        // MemberSeat 생성
        MemberSeat memberSeat = newMemberSeat(memberId, concertId, seatId);
        memberSeatCommandRepository.save(memberSeat);
    }

    /*
        좌석 접수 취소
     */
    public void cancelReception(Long memberId, Long concertId, Long seatId) {

        // MemberSeat 조회
        MemberSeat memberSeat = getMemberSeatOrNull(memberId, concertId, seatId);
        
        // 접수 내역 확인
        if (memberSeat == null) {
            throw new Exception400("해당 좌석을 접수하신 내역을 찾을 수 없습니다.");
        };

        // MemberSeat 삭제
        memberSeatCommandRepository.delete(memberSeat);
    }

    // MemberSeat 조회
    private MemberSeat getMemberSeatOrNull(Long memberId, Long concertId, Long seatId) {
        return memberSeatQueryRepository.findByMemberIdAndConcertIdAndSeatId(
                memberId, concertId, seatId
        ).orElse(null);
    }

    // MemberSeat 생성
    private static MemberSeat newMemberSeat(Long memberId, Long concertId, Long seatId) {
        return MemberSeat.builder()
                .memberId(memberId)
                .concertId(concertId)
                .seatId(seatId)
                .memberSeatStatus(MemberSeatStatus.RECEIVED)
                .build();
    }
}
