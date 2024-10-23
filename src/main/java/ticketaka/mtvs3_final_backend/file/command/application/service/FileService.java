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
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FileResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForAuthRedisRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadRedisRepository;

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

    private final FileRepository fileRepository;
    private final FileUploadRedisRepository fileUploadRedisRepository;
    private final FileUploadForAuthRedisRepository fileUploadForAuthRedisRepository;

    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    /*
        파일 업로드 - 회원 가입 용
    */
    public void uploadImgForSignUp(FileResponseDTO.uploadImgForSignUpDTO requestDTO) {

        String imgUrl = uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        setFileUploadForSignUp(requestDTO.email(), requestDTO.secondPwd(), imgUrl);
    }

    /*
        파일 업로드 - 회원 인증 용
    */
    public FileUploadForAuth uploadImgForVerification(FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        String imgUrl = uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        return setFileUploadForAuth(requestDTO.code(), imgUrl);
    }

    // 회원 가입 용 FileUploadForAuth 수정
    private void setFileUploadForSignUp(String email, String secondPwd, String imgUrl) {

        FileUploadForAuth fileUpload = getFileUploadForAuth(email);

        fileUpload.setImgUrl(imgUrl);
        fileUpload.setCode(secondPwd);
        fileUpload.setUploadStatus(UploadStatus.SUCCESS);

        fileUploadForAuthRedisRepository.save(fileUpload);
    }

    // 회원 인증 용 FileUploadForAuth 수정
    private FileUploadForAuth setFileUploadForAuth(String code, String imgUrl) {

        FileUploadForAuth fileUpload = getFileUploadForAuth(code);

        fileUpload.setImgUrl(imgUrl);
        fileUpload.setUploadStatus(UploadStatus.UPLOADED);

        return fileUploadForAuthRedisRepository.save(fileUpload);
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

    // File 객체 생성
    public void newFile(RelationType relationType, Long id, String imgUrl, FilePurpose filePurpose) {

        File file = File.builder()
                .relationType(relationType)
                .relationId(id)
                .fileUrl(imgUrl)
                .filePurpose(filePurpose)
                .build();

        fileRepository.save(file);
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

    private FileUploadForAuth getFileUploadForAuth(String id) {

        System.out.println("id = " + id);

        return fileUploadForAuthRedisRepository.findById(id)
                .orElseThrow(() -> new Exception400("파일 업로드 대기 상태가 아닙니다."));
    }
}
