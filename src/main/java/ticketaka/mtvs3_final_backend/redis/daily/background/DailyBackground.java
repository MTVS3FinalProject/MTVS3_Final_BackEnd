package ticketaka.mtvs3_final_backend.redis.daily.background;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "draw_result", timeToLive = 24 * 60 * 60) // 60 * 15
public class DailyBackground {

    @Id
    private String id;
    @Setter
    private Integer refreshCount;
    @Setter
    private LocalDate lastRefreshDate;

    @Builder
    public DailyBackground(String id, Integer refreshCount, LocalDate lastRefreshDate) {
        this.id = id;
        this.refreshCount = refreshCount;
        this.lastRefreshDate = lastRefreshDate;
    }
}
