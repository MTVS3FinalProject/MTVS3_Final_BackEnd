package ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;

@Repository
public interface TicketCommandRepository extends JpaRepository<Ticket, Long> {
}
