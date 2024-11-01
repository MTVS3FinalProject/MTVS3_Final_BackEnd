package ticketaka.mtvs3_final_backend.ticketing.memberseat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.repository.MemberSeatCommandRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberSeatCommandService {

    private final MemberSeatCommandRepository memberSeatCommandRepository;

    /*
        좌석 접수
     */
    @Transactional
    public void seatReception(Long memberId, Long concertId, Long seatId) {
        MemberSeat memberSeat = newMemberSeat(memberId, concertId, seatId);
        memberSeatCommandRepository.save(memberSeat);
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
