package ticketaka.mtvs3_final_backend.ticketing.seat.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatQueryRepository extends JpaRepository<Seat, Long> {

    // Member 가 해당 Concert 에서 접수한 Seat 목록 조회
    @Query("SELECT s FROM Seat s " +
            "JOIN MemberSeat ms ON ms.seatId = s.id " +
            "WHERE ms.memberId = :memberId " +
            "AND ms.concertId = :concertId " +
            "AND ms.memberSeatStatus = :status")
    List<Seat> findAllSeatsByMemberIdAndConcertIdAndMemberSeatStatus(@Param("memberId") Long memberId,
                                                                     @Param("concertId") Long concertId,
                                                                     @Param("status") MemberSeatStatus status);

    // 접수 가능한 Seat 조회
    @Query("SELECT s FROM Seat s " +
            "WHERE s.concert.id = :concertId " +
            "AND s.id = :seatId " +
            "AND s.seatStatus = :status")
    Optional<Seat> findByConcertIdAndIdAndSeatStatus(@Param("concertId") Long concertId,
                                                     @Param("seatId") Long seatId,
                                                     @Param("status") SeatStatus seatStatus);
}
