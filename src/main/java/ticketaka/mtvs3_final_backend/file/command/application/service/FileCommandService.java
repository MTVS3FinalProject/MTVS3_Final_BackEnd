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
import ticketaka.mtvs3_final_backend.file.command.domain.model.Background;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileCommandRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForAuthRedisRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileCommandService {

    private final FileCommandRepository fileCommandRepository;
    private final FileUploadForAuthRedisRepository fileUploadForAuthRedisRepository;

    @Value("${FIREBASE.STORAGE}")
    private String firebaseStorageUrl;

    private static final String IMAGE_CONTENT_TYPE = "image/png";
    private static final String AI_BACKGROUND_FILENAME_PREFIX = "AI_BACKGROUND_";
    private static final String CUSTOM_TICKET_FILENAME_PREFIX = "AI_BACKGROUND_";

    /*
        파일 업로드 - 회원 인증 용
    */
    public FileUploadForAuth uploadImgForVerification(FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        String imgUrl = uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        return setFileUploadForAuth(requestDTO.code(), imgUrl);
    }

    // AI 배경 이미지 저장
    public void saveAIBackgroundImage(Long backgroundId, byte[] backgroundImage) {

        String fileName = AI_BACKGROUND_FILENAME_PREFIX + System.currentTimeMillis();
        String fileUrl = uploadImgByByte(backgroundImage, fileName, IMAGE_CONTENT_TYPE);

        // File 생성 및 저장
        newFile(RelationType.BACKGROUND, backgroundId, fileUrl, FilePurpose.CUSTOM);
    }

    // Custom Ticket 이미지 저장
    public void saveCustomTicketImage(Long customTicketId, byte[] customTicketImage) {

        String fileName = CUSTOM_TICKET_FILENAME_PREFIX + System.currentTimeMillis();
        String fileUrl = uploadImgByByte(customTicketImage, fileName, IMAGE_CONTENT_TYPE);

        // File 생성 및 저장
        newFile(RelationType.CUSTOM_TICKET, customTicketId, fileUrl, FilePurpose.CUSTOM);
    }

    // 회원 가입 용 FileUploadForAuth 수정
    protected void setFileUploadForSignUp(String email, String secondPwd, String imgUrl) {

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
    protected String uploadImg(MultipartFile image, String fileName) {

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

    // 파일 업로드 기능 - byte[]
    protected String uploadImgByByte(byte[] imageData, String fileName, String contentType) {
        
        // 이미지 압축
        byte[] compressedData = compressImageData(imageData);

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        Blob blob = bucket.create(fileName,
                compressedData, contentType);

        String fileUrl = blob.getMediaLink(); // 파이어베이스에 저장된 파일 url

        log.info("File Url : {}", fileUrl);

        return fileUrl;
    }

    // 이미지 압축
    private byte[] compressImageData(byte[] imageData) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            // BufferedImage 로 변환
            BufferedImage image = ImageIO.read(byteArrayInputStream);
            ImageIO.write(image, "png", byteArrayOutputStream);

            // 압축 수행
            ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(compressedOutputStream, new Deflater(Deflater.BEST_COMPRESSION))) {
                deflaterOutputStream.write(byteArrayOutputStream.toByteArray());
            }

            return compressedOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error during image compression", e);
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

        fileCommandRepository.save(file);
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
