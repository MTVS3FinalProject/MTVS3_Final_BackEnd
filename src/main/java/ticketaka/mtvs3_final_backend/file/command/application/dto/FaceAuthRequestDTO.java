package ticketaka.mtvs3_final_backend.file.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

public class FaceAuthRequestDTO {

    // 파일 업로드
    public record verificationMemberDTO(
            MultipartFile image,
            String code,
            String secondPwd
    ) {
    }

    // 얼굴 인식
    public record recognizeFaceDTO(
            String faceImg
    ) {
    }

    // 얼굴 인증
    public record identifyFaceDTO(
            String originImg,
            String currentImg
    ) {
    }
}
