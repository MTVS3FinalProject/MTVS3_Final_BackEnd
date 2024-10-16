package ticketaka.mtvs3_final_backend.redis.FileUpload.repository;

import org.springframework.data.repository.CrudRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForSignUp;

import java.util.Optional;

public interface FileUploadForSignUpRedisRepository extends CrudRepository<FileUploadForSignUp, String> {

    Optional<FileUploadForSignUp> findByEmail(String email);
}
