package ticketaka.mtvs3_final_backend.file.command.infrastructure.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
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

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile file, String fileName, String contentType) {

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
}
