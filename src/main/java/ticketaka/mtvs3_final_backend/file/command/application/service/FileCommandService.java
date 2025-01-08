package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception500;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.BufferedImageMultipartFile;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileCommandRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.service.S3Service;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForAuthRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto.TicketCommandRequestDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileCommandService {

    private final S3Service s3Service;

    private final FileCommandRepository fileCommandRepository;
    private final FileUploadForAuthRedisRepository fileUploadForAuthRedisRepository;

    private static final String IMAGE_CONTENT_TYPE = "image/png";

    private static final String STICKER_FILENAME_PREFIX = "STICKER_";
    private static final String AI_BACKGROUND_FILENAME_PREFIX = "AI_BACKGROUND_";
    private static final String CUSTOM_TICKET_FILENAME_PREFIX = "CUSTOM_TICKET_";

    private static final Integer CUSTOM_TICKET_WIDTH = 504;
    private static final Integer CUSTOM_TICKET_HEIGHT = 888;

    /*
        파일 업로드 - 회원 인증 용
    */
    public FileUploadForAuth uploadImgForVerification(FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        String imgUrl = s3Service.uploadImageByFireBase(requestDTO.image(), requestDTO.image().getOriginalFilename(), IMAGE_CONTENT_TYPE);

        return setFileUploadForAuth(requestDTO.code(), imgUrl);
    }

    // Sticker 이미지 저장
    public void saveStickerImage(Long stickerId, MultipartFile stickerImage) {

        String fileName = STICKER_FILENAME_PREFIX + stickerId + System.currentTimeMillis();
        String fileUrl = s3Service.uploadImageByFireBase(stickerImage, fileName, IMAGE_CONTENT_TYPE);

        // File 생성 및 저장
        newFile(RelationType.STICKER, stickerId, fileUrl, FilePurpose.CUSTOM);
    }

    // AI 배경 이미지 저장
    public File saveAIBackgroundImage(Long backgroundId, MultipartFile backgroundImage) {

        String fileName = AI_BACKGROUND_FILENAME_PREFIX + backgroundId + System.currentTimeMillis();
        String fileUrl = s3Service.uploadImageByFireBase(backgroundImage, fileName, IMAGE_CONTENT_TYPE);

        // File 생성 및 저장
        return newFile(RelationType.BACKGROUND, backgroundId, fileUrl, FilePurpose.CUSTOM);
    }

    // Custom Ticket 이미지 저장
    public void saveCustomTicketImage(Long customTicketId, TicketCommandRequestDTO.saveCustomTicketDTO requestDTO) {

        // Crop Custom Ticket
        MultipartFile customTicketImage = cropCustomTicketImage(requestDTO);

        String fileName = CUSTOM_TICKET_FILENAME_PREFIX + System.currentTimeMillis();
        String fileUrl = s3Service.uploadImageByFireBase(customTicketImage, fileName, IMAGE_CONTENT_TYPE);

        // File 생성 및 저장
        newFile(RelationType.CUSTOM_TICKET, customTicketId, fileUrl, FilePurpose.CUSTOM);
    }

    // TicketQR 업로드
    public void saveTicketQRImage(Long ticketId, MultipartFile ticketQRData) {

        String fileUrl = s3Service.uploadImageByFireBase(ticketQRData, ticketQRData.getOriginalFilename(), IMAGE_CONTENT_TYPE);

        newFile(RelationType.TICKET, ticketId, fileUrl, FilePurpose.VERIFICATION);
    }

    // Crop Custom Ticket
    public MultipartFile cropCustomTicketImage(TicketCommandRequestDTO.saveCustomTicketDTO requestDTO) {
        try {
            MultipartFile multipartFile = requestDTO.customTicketImage();
            BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());

            log.info("Original image size: {} {}", originalImage.getWidth(), originalImage.getHeight());

            int x = requestDTO.start_x();
            int y = requestDTO.start_y();

            if (x + CUSTOM_TICKET_WIDTH > originalImage.getWidth() || y + CUSTOM_TICKET_HEIGHT > originalImage.getHeight()) {
                throw new Exception400("원본 이미지를 초과하여 자를 수 없습니다.");
            }

            // 특정 영역 자르기
            BufferedImage croppedImage = originalImage.getSubimage(x, y, CUSTOM_TICKET_WIDTH, CUSTOM_TICKET_HEIGHT);

            return new BufferedImageMultipartFile(
                    croppedImage,
                    multipartFile.getOriginalFilename(),
                    "png",
                    IMAGE_CONTENT_TYPE
            );
        } catch (IOException e) {
            throw new Exception500(e.getMessage());
        }
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
    public String uploadImg(MultipartFile image, String fileName) {

        return s3Service.uploadImageByFireBase(image, fileName, IMAGE_CONTENT_TYPE);
    }

    // File 객체 생성
    public File newFile(RelationType relationType, Long id, String imgUrl, FilePurpose filePurpose) {

        File file = File.builder()
                .relationType(relationType)
                .relationId(id)
                .fileUrl(imgUrl)
                .filePurpose(filePurpose)
                .build();

        fileCommandRepository.save(file);

        return file;
    }

    private FileUploadForAuth getFileUploadForAuth(String id) {

        System.out.println("id = " + id);

        return fileUploadForAuthRedisRepository.findById(id)
                .orElseThrow(() -> new Exception400("파일 업로드 대기 상태가 아닙니다."));
    }

    public BufferedImage convertImageDataToBufferedImage(byte[] backgroundImageData) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(backgroundImageData)) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                throw new RuntimeException("Invalid image data received");
            }
            return bufferedImage;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert byte array to BufferedImage", e);
        }
    }
}
