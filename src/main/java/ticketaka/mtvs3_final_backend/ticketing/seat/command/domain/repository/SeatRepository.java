package ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByConcertAndSeatStatus(Concert concert, SeatStatus seatStatus);

    Optional<Seat> findByIdAndConcert(Long seatId, Concert concert);

    Optional<Seat>  findByConcertIdAndIdAndSeatStatus(Long concertId, Long seatId, SeatStatus seatStatus);
}
