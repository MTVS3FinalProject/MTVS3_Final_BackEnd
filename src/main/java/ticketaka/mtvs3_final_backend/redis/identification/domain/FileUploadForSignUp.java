package ticketaka.mtvs3_final_backend.redis.identification.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "fileupload_for_signup", timeToLive = 900) // 60 * 15
public class FileUploadForSignUp extends FileUpload {

    @Setter
    private String imgUrl;

    @Builder
    public FileUploadForSignUp(String id, UploadStatus uploadStatus, String imgUrl) {
        super(id, uploadStatus);
        this.imgUrl = imgUrl;
    }
}
