package ticketaka.mtvs3_final_backend.seat.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_seat_tb")
public class MemberSeat extends BaseTimeEntity {

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
    private MemberSeatStatus memberSeatStatus;

    @Builder
    public MemberSeat(Long memberId, Long concertId, Long seatId, MemberSeatStatus memberSeatStatus) {
        this.memberId = memberId;
        this.concertId = concertId;
        this.seatId = seatId;
        this.memberSeatStatus = memberSeatStatus;
    }
}
