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
    private IdentificationStatus identificationStatus;

    @Builder
    public Identification(String email, String imgUrl, IdentificationStatus identificationStatus) {
        this.id = email;
        this.imgUrl = imgUrl;
        this.identificationStatus = identificationStatus;
    }
}