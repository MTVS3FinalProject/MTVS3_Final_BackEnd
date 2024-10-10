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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    // 파일 업로드
    public void uploadFirebaseBucket(MultipartFile multipartFile, String fileName) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        Blob blob = bucket.create(fileName,
                multipartFile.getInputStream(), multipartFile.getContentType());

        String fileUrl = blob.getMediaLink(); // 파이어베이스에 저장된 파일 url

        log.info("File Url : {}", fileUrl);

        // byte[] 로 테스트할 경우 사용
        // byte[] imageData = getImageFromUrl(fileUrl);
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
}
