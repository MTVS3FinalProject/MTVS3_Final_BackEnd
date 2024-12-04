package ticketaka.mtvs3_final_backend.hall.tree.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.hall.tree.command.domain.model.TicketTree;

@Repository
public interface TicketTreeCommandRepository extends JpaRepository<TicketTree, Long> {
}
