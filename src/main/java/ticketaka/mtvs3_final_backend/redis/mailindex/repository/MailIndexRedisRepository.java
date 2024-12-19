package ticketaka.mtvs3_final_backend.redis.mailindex.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.mailindex.domain.MailIndex;

public interface MailIndexRedisRepository extends CrudRepository<MailIndex, String> {
}
