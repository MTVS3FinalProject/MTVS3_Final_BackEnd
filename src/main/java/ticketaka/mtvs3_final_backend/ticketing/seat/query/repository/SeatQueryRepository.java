package ticketaka.mtvs3_final_backend.ticketing.seat.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;

@Repository
public interface SeatQueryRepository extends JpaRepository<Seat, Long> {
}
