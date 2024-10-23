package ticketaka.mtvs3_final_backend.redis.drawing.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "draw_result", timeToLive = 900) // 60 * 15
public class DrawResult {

    @Id
    private String id;
    @Setter
    private Long concertId;
    @Setter
    private Long seatId;
    @Setter
    private PaymentStatus paymentStatus;

    @Builder
    public DrawResult(String id, Long concertId, Long seatId, PaymentStatus paymentStatus) {
        this.id = id;
        this.concertId = concertId;
        this.seatId = seatId;
        this.paymentStatus = paymentStatus;
    }
}
