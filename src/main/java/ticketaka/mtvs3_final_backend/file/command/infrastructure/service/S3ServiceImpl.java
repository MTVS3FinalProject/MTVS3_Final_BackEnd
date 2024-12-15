package ticketaka.mtvs3_final_backend.file.command.infrastructure.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend.file.command.domain.service.S3Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    @Value("${AWS.S3.BUCKET}")
    private String bucket;
    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    @Override
    public String uploadImageByS3(MultipartFile file, String fileName, String contentType) {

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(
                    bucket, fileName, file.getInputStream(), objectMetadata
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Override
    public String uploadImageByFireBase(MultipartFile file, String fileName, String contentType) {

        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

            Blob blob = bucket.create(fileName, file.getInputStream(), contentType);

            return blob.getMediaLink();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
