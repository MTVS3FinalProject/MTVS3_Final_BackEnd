package ticketaka.mtvs3_final_backend.redis.seat.postpone.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "seat_postpone", timeToLive = 60 * 60 * 24) // 60 * 15
public class SeatPostpone {

    @Id
    private String id;

    private Long memberId;
    private Long concertId;
    private Long seatId;

    @Builder
    public SeatPostpone(String id, Long memberId, Long concertId, Long seatId) {
        this.id = id;
        this.memberId = memberId;
        this.concertId = concertId;
        this.seatId = seatId;
    }
}
