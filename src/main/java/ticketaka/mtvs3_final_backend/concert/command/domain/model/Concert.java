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
    private int receptionLimit;
    @Column
    private LocalDateTime concertDate;

    @Builder
    public Concert(String name, int receptionLimit, LocalDateTime concertDate) {
        this.name = name;
        this.receptionLimit = receptionLimit;
        this.concertDate = concertDate;
    }
}
