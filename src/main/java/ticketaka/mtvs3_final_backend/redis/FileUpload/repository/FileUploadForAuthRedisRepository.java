package ticketaka.mtvs3_final_backend.redis.FileUpload.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;

import java.util.Optional;

public interface FileUploadForAuthRedisRepository extends CrudRepository<FileUploadForAuth, String> {

    Optional<FileUploadForAuth> findById(String id);
}
