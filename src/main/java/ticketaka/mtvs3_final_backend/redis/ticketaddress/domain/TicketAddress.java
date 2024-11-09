package ticketaka.mtvs3_final_backend.redis.ticketaddress.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "ticket_address", timeToLive = 900) // 60 * 15
public class TicketAddress {

    @Id
    private String id;

    private String userName;
    private String userPhoneNumber;
    private String userAddress1;
    private String userAddress2;

    @Builder
    public TicketAddress(String id, String userName, String userPhoneNumber, String userAddress1, String userAddress2) {
        this.id = id;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userAddress1 = userAddress1;
        this.userAddress2 = userAddress2;
    }

    public static String generateTicketAddressId(Long memberId, Long concertId, Long seatId) {
        return memberId + "_" + concertId + "_" + seatId;
    }
}
