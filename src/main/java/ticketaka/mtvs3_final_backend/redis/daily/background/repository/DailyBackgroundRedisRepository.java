package ticketaka.mtvs3_final_backend.redis.daily.background.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.daily.background.domain.DailyBackground;

public interface DailyBackgroundRedisRepository extends CrudRepository<DailyBackground, String> {

}
