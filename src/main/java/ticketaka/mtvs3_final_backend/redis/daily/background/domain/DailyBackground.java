package ticketaka.mtvs3_final_backend.redis.daily.background.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "daily_background", timeToLive = 24 * 60 * 60) // 1일
public class DailyBackground {

    @Id
    private String id;
    @Setter
    private Integer refreshCount;
    @Setter
    private LocalDate lastRefreshDate;

    @Builder
    public DailyBackground(String id, LocalDate lastRefreshDate) {
        this.id = id;
        this.refreshCount = DAILY_BACKGROUND_GENERATION_LIMIT;
        this.lastRefreshDate = lastRefreshDate;
    }

    private static final Integer DAILY_BACKGROUND_GENERATION_LIMIT = 2;
}
