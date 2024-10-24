package ticketaka.mtvs3_final_backend.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;

import java.util.Optional;

@Repository
public interface MemberSeatRepository extends JpaRepository<MemberSeat, Long> {

    Long countByConcertIdAndSeatIdAndMemberSeatStatus(Long concertId, Long seatId, MemberSeatStatus status);

    int countByMemberIdAndConcertId(Long memberId, Long concertId);

    Optional<MemberSeat> findByMemberIdAndConcertIdAndSeatIdAndMemberSeatStatus(Long memberId, Long concertId, Long seatId, MemberSeatStatus status);

    Optional<MemberSeat> findFirstByMemberIdAndConcertIdAndMemberSeatStatus(Long memberId, Long concertId, MemberSeatStatus memberSeatStatus);
}
