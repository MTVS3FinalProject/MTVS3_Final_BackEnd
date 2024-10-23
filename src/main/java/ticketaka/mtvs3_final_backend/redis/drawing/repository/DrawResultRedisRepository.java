package ticketaka.mtvs3_final_backend.redis.drawing.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;

public interface DrawResultRedisRepository extends CrudRepository<DrawResult, String> {
}
