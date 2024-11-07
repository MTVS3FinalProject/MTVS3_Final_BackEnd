package ticketaka.mtvs3_final_backend.redis.daily.background.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.Background;

public interface BackgroundRedisRepository extends CrudRepository<Background, String> {
}
