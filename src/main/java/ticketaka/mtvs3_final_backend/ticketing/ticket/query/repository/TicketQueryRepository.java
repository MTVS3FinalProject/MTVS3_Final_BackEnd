package ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;

@Repository
public interface TicketQueryRepository extends JpaRepository<Ticket, Long> {
}
