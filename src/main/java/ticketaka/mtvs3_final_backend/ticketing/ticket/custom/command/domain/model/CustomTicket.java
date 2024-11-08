package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom_ticket_tb")
public class CustomTicket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column
    @ElementCollection
    private List<Long> stickerIdList;
    @Column(nullable = false)
    private Long backgroundId;

    @Builder
    public CustomTicket(Long ticketId, List<Long> stickerIdList, Long backgroundId) {
        this.ticketId = ticketId;
        this.stickerIdList = stickerIdList;
        this.backgroundId = backgroundId;
    }
}
