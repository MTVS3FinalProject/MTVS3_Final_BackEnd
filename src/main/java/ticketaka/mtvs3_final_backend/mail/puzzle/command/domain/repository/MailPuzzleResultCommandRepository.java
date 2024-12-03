package ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.model.MailPuzzleResult;

@Repository
public interface MailPuzzleResultCommandRepository extends JpaRepository<MailPuzzleResult, Long> {
}
