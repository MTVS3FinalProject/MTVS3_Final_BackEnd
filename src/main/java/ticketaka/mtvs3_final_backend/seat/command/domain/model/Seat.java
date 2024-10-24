package ticketaka.mtvs3_final_backend.seat.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat_tb")
public class Seat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int floor;
    @Column
    private String section;
    @Column
    private String number;
    @Column
    private int price;
    @Column
    private LocalDateTime drawingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    @Builder
    public Seat(int floor, String section, String number, int price, LocalDateTime drawingTime, Concert concert, SeatStatus seatStatus) {
        this.floor = floor;
        this.section = section;
        this.number = number;
        this.price = price;
        this.drawingTime = drawingTime;
        this.concert = concert;
        this.seatStatus = seatStatus;
    }
}
