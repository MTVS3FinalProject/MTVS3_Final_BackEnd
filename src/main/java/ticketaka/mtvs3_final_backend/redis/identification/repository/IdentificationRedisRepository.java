package ticketaka.mtvs3_final_backend.redis.identification.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.identification.domain.Identification;

public interface IdentificationRedisRepository extends CrudRepository<Identification, Long> {

    Identification findByEmail(String email);
}
