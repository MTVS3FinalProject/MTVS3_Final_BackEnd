package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

import java.util.ArrayList;
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

    @Setter
    @Column
    @ElementCollection
    private List<Long> stickerIdList;
    @Setter
    @Column
    private Long backgroundId;

    @Builder
    public CustomTicket(Long ticketId) {
        this.ticketId = ticketId;
        this.stickerIdList = new ArrayList<>();
        this.backgroundId = null;
    }
}
