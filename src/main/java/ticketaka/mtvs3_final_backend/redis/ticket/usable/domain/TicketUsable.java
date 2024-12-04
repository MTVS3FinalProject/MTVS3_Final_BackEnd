package ticketaka.mtvs3_final_backend.redis.ticket.usable.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "ticket_usable", timeToLive = 60 * 5)
public class TicketUsable {

    @Id
    private String id;

    private Long memberId;

    @Builder
    public TicketUsable(Long ticketId, Long memberId) {
        this.id = ticketId.toString();
        this.memberId = memberId;
    }
}
