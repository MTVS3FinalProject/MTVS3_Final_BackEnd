package ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mail_puzzle_result_tb")
public class MailPuzzleResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mailId;
    @Column(nullable = false)
    private Long puzzleResultId;

    @Builder
    public MailPuzzleResult(Long mailId, Long puzzleResultId) {
        this.mailId = mailId;
        this.puzzleResultId = puzzleResultId;
    }
}
