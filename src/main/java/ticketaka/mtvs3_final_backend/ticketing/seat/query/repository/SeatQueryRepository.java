package ticketaka.mtvs3_final_backend.ticketing.seat.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;

import java.util.List;

@Repository
public interface SeatQueryRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s " +
            "JOIN MemberSeat ms ON ms.seatId = s.id " +
            "WHERE ms.memberId = :memberId " +
            "AND ms.concertId = :concertId " +
            "AND ms.memberSeatStatus = :status")
    List<Seat> findAllSeatsByMemberIdAndConcertId(@Param("memberId") Long memberId,
                                                  @Param("concertId") Long concertId,
                                                  @Param("status") MemberSeatStatus status);
}
