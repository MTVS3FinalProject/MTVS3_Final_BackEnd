package ticketaka.mtvs3_final_backend.redis.mailindex.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 259200) // 60 * 60 * 24 * 3
public class MailIndex {

    @Id
    private String id;
    @Setter
    private Long mailIndex;

    @Builder
    public MailIndex(String id, Long mailIndex) {
        this.id = id;
        this.mailIndex = mailIndex;
    }
}
