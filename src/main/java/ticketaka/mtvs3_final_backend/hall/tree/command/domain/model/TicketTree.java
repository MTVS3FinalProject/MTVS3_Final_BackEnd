package ticketaka.mtvs3_final_backend.hall.tree.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket_tree_tb")
public class TicketTree extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;
    @Column
    private Long fileId;

    @Builder
    public TicketTree(Long memberId, Long fileId) {
        this.memberId = memberId;
        this.fileId = fileId;
    }
}
