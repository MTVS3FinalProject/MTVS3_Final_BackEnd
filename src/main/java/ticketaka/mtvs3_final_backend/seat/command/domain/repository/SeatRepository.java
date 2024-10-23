package ticketaka.mtvs3_final_backend.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByConcertAndSeatStatus(Concert concert, SeatStatus seatStatus);

    Optional<Seat> findByConcertAndSectionAndNumber(Concert concert, String section, String number);

    @Query("SELECT s FROM Seat s " +
            "JOIN MemberSeat ms ON ms.seatId = s.id " +
            "WHERE ms.memberId = :memberId " +
            "AND ms.concertId = :concertId " +
            "AND ms.memberSeatStatus = :status")
    List<Seat> findAllSeatsByMemberIdAndConcertIdAndStatus(@Param("memberId") Long memberId,
                                                           @Param("concertId") Long concertId,
                                                           @Param("status") MemberSeatStatus status);
}
