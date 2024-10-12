package ticketaka.mtvs3_final_backend.redis.refreshtoken.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.refreshtoken.domain.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String refreshToken);
}
