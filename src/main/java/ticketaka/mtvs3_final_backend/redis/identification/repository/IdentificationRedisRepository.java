package ticketaka.mtvs3_final_backend.redis.identification.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.identification.domain.FileUploadStatus;

import java.util.Optional;

public interface IdentificationRedisRepository extends CrudRepository<FileUploadStatus, Long> {

    Optional<FileUploadStatus> findByEmail(String email);
}
