package ticketaka.mtvs3_final_backend.concert.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private int ageRestriction;
    @Column
    private int receptionLimit;

    @Builder
    public Concert(String name, LocalDateTime concertDate, int ageRestriction, int receptionLimit) {
        this.name = name;
        this.concertDate = concertDate;
        this.ageRestriction = ageRestriction;
        this.receptionLimit = receptionLimit;
    }
}
