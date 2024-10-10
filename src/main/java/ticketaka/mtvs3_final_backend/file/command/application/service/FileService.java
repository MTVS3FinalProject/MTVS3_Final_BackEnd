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

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    // 파일 업로드
    public String uploadFirebaseBucket(MultipartFile multipartFile, String fileName) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        Blob blob = bucket.create(fileName,
                multipartFile.getInputStream(), multipartFile.getContentType());

        return blob.getMediaLink(); // 파이어베이스에 저장된 파일 url
    }

    // 파일 삭제
    public void deleteFirebaseBucket(String key) {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        bucket.get(key).delete();
    }
}
