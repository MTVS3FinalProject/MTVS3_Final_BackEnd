package ticketaka.mtvs3_final_backend.redis.identification.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.identification.domain.FileUpload;

import java.util.Optional;

public interface IdentificationRedisRepository extends CrudRepository<FileUpload, Long> {

    Optional<FileUpload> findByEmail(String email);
}
