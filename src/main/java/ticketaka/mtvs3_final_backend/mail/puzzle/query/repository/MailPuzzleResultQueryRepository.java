package ticketaka.mtvs3_final_backend.mail.puzzle.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.model.MailPuzzleResult;

@Repository
public interface MailPuzzleResultQueryRepository extends JpaRepository<MailPuzzleResult, Long> {
}
