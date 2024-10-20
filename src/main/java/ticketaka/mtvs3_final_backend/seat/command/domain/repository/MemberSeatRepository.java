package ticketaka.mtvs3_final_backend.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;

import java.util.List;

@Repository
public interface MemberSeatRepository extends JpaRepository<MemberSeat, Long> {

    Long countByConcertIdAndSeatIdAndMemberSeatStatus(Long concertId, Long seatId, MemberSeatStatus status);

    int countByMemberIdAndConcertId(Long currentMemberId, Long concertId);
}
