package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.CustomTicket;

@Repository
public interface TicketCustomQueryRepository extends JpaRepository<CustomTicket, Long> {
}
