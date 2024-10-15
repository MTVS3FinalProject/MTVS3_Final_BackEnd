package ticketaka.mtvs3_final_backend.file.command.application.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.redis.identification.domain.FileUpload;
import ticketaka.mtvs3_final_backend.redis.identification.domain.FileUploadForSignUp;
import ticketaka.mtvs3_final_backend.redis.identification.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.identification.repository.FileUploadRedisRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final FileUploadRedisRepository fileUploadRedisRepository;

    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    // 파일 업로드 - 테스트 용
    public void uploadFirebaseBucket(MultipartFile multipartFile, String fileName) {

        uploadImg(multipartFile, fileName);
    }

    // ImageUrl 을 통해 byte[] 가져오기 (HTTP 요청 사용)
    private byte[] getImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }

    // 파일 삭제
    public void deleteFirebaseBucket(String key) {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        bucket.get(key).delete();
    }

    // 파일 업로드 - 회원 가입 용
    public void uploadImgForSignUp(MultipartFile image, String fileName, String email) {

        String imgUrl = uploadImg(image, fileName);

        UploadMemberUrl(email, imgUrl);
    }

    private void UploadMemberUrl(String email, String imgUrl) {

        FileUploadForSignUp fileUpload = (FileUploadForSignUp) fileUploadRedisRepository.findById(email)
                .orElseThrow(() -> new Exception400("이메일 기록을 찾을 수 없습니다."));

        fileUpload.setImgUrl(imgUrl);
        fileUpload.setUploadStatus(UploadStatus.COMPLETED);
        fileUploadRedisRepository.save(fileUpload);
    }

    // 파일 업로드 기능
    private String uploadImg(MultipartFile image, String fileName) {

        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

            Blob blob = bucket.create(fileName,
                    image.getInputStream(), image.getContentType());

            String fileUrl = blob.getMediaLink(); // 파이어베이스에 저장된 파일 url

            log.info("File Url : {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
