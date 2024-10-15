package ticketaka.mtvs3_final_backend.redis.identification.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.identification.domain.Identification;

import java.util.Optional;

public interface IdentificationRedisRepository extends CrudRepository<Identification, Long> {

    Optional<Identification> findById(String email);
}
