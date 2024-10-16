package ticketaka.mtvs3_final_backend.redis.FileUpload.domain;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@RedisHash(value = "fileupload", timeToLive = 900) // 60 * 15
public class FileUpload {

    @Id
    private String id;
    @Setter
    private UploadStatus uploadStatus;

    public FileUpload(String id, UploadStatus uploadStatus) {
        this.id = id;
        this.uploadStatus = uploadStatus;
    }
}
