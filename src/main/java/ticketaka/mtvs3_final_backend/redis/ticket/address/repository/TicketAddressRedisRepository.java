package ticketaka.mtvs3_final_backend.redis.ticket.address.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.ticket.address.domain.TicketAddress;

public interface TicketAddressRedisRepository extends CrudRepository<TicketAddress, String> {
}
