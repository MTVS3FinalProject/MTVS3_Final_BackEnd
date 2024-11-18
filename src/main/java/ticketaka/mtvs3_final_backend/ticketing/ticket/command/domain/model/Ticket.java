package ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileCommandService;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket_tb")
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Long concertId;
    @Column(nullable = false)
    private Long seatId;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @Column(nullable = false, unique = true)
    private String ticketNumber;
    @Column(nullable = false)
    private Integer ticketPrice;

    @Column
    private LocalDateTime issuedTime;

    @Builder
    public Ticket(Long memberId, Long concertId, Long seatId, String ticketNumber, Integer ticketPrice) {
        this.memberId = memberId;
        this.concertId = concertId;
        this.seatId = seatId;
        this.ticketStatus = TicketStatus.RESERVE;
        this.ticketNumber = ticketNumber;
        this.ticketPrice = ticketPrice;
        this.issuedTime = null;
    }
}
