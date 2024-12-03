package ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.model.PuzzleResult;

@Repository
public interface PuzzleResultCommandRepository extends JpaRepository<PuzzleResult, Long> {
}
