package ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;

import java.util.Optional;

@Repository
public interface MemberSeatQueryRepository extends JpaRepository<MemberSeat, Long> {

    // 해당 좌석에 접수한 회원 수 조회
    int countByConcertIdAndSeatIdAndMemberSeatStatus(Long concertId, Long seatId, MemberSeatStatus status);

    // MemberSeat 조회
    Optional<MemberSeat> findByMemberIdAndConcertIdAndSeatId(Long memberId, Long concertId, Long seatId);

    // 이미 접수된 Seat 인지 검사
    Optional<MemberSeat> findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(Long currentMemberId, Long concertId, Long seatId, MemberSeatStatus memberSeatStatus);
}
