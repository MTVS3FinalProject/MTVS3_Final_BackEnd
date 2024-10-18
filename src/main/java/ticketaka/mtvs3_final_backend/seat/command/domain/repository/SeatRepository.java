package ticketaka.mtvs3_final_backend.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
