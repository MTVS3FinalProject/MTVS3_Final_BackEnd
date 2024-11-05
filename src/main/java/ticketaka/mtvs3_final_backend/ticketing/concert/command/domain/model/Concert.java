package ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_tb")
public class Concert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private LocalDateTime concertDate;
    @Column
    private Integer ageRestriction;
    @Column
    private Integer receptionLimit;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private ConcertStatus concertStatus;

    @Builder
    public Concert(String name, LocalDateTime concertDate, Integer ageRestriction, Integer receptionLimit) {
        this.name = name;
        this.concertDate = concertDate;
        this.ageRestriction = ageRestriction;
        this.receptionLimit = receptionLimit;
        this.concertStatus = ConcertStatus.PREPARING;
    }
}
