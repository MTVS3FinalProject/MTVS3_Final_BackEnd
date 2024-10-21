package ticketaka.mtvs3_final_backend.redis.FileUpload.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@SuperBuilder
@RedisHash(value = "fileupload_for_signup", timeToLive = 900) // 60 * 15
public class FileUploadForAuth extends FileUpload {
    @Setter
    private String code;

    public FileUploadForAuth(String email, UploadStatus uploadStatus, String code) {
        super(email, uploadStatus);
        this.code = code;
    }
}
