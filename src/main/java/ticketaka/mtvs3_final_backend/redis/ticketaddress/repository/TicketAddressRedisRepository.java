package ticketaka.mtvs3_final_backend.redis.ticketaddress.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.ticketaddress.domain.TicketAddress;

public interface TicketAddressRedisRepository extends CrudRepository<TicketAddress, String> {
}
