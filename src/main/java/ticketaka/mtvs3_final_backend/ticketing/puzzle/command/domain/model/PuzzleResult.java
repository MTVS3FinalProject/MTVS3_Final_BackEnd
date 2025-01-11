package ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "puzzle_result_tb")
public class PuzzleResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long concertId;
    @Column
    private Long titleId;
    @Column
    private Long stickerId;

    @Column
    private Integer ranking;

    @Builder
    public PuzzleResult(Long concertId, Long titleId, Long stickerId, Integer ranking) {
        this.concertId = concertId;
        this.titleId = titleId;
        this.stickerId = stickerId;
        this.ranking = ranking;
    }
}
