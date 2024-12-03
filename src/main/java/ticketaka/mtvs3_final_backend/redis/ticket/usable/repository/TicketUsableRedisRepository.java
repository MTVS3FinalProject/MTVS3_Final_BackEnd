package ticketaka.mtvs3_final_backend.redis.ticket.usable.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.ticket.usable.domain.TicketUsable;

public interface TicketUsableRedisRepository extends CrudRepository<TicketUsable, String> {
}
