package ticketaka.mtvs3_final_backend.redis.seat.postpone.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.seat.postpone.domain.SeatPostpone;

public interface SeatPostponeRedisRepository extends CrudRepository<SeatPostpone, String> {
}
