package ticketaka.mtvs3_final_backend.redis.identification.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "identification", timeToLive = 900) // 60 * 15
public class Identification {

    @Id
    private String id;
    @Setter
    private String imgUrl;
    @Setter
    private IdentificationStatus status;

    @Builder
    public Identification(String email, String imgUrl) {
        this.id = email;
        this.imgUrl = imgUrl;
    }
}
