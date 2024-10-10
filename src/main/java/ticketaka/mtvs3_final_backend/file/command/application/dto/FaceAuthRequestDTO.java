package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class FaceAuthRequestDTO {

    // 얼굴 인증
    public record identifyFaceDTO(
            String originImg,
            String currentImg
    ) {
    }
}
