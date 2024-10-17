package ticketaka.mtvs3_final_backend.redis.FileUpload.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUpload;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForSignUp;

import java.util.Optional;

public interface FileUploadRedisRepository extends CrudRepository<FileUpload, Long> {

    Optional<FileUpload> findById(String id);
}
