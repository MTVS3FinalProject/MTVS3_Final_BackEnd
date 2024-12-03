package ticketaka.mtvs3_final_backend.ticketing.puzzle.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.model.PuzzleResult;

@Repository
public interface PuzzleResultQueryRepository extends JpaRepository<PuzzleResult, Long> {

    @Query("SELECT pr " +
            "FROM PuzzleResult pr " +
            "JOIN MailPuzzleResult mpr ON pr.id = mpr.puzzleResultId " +
            "WHERE mpr.mailId = :mailId")
    PuzzleResult getPuzzleResultByMailId(@Param("mailId") Long mailId);
}
