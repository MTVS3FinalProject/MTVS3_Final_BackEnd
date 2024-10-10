package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class FaceAuthRequestDTO {

    // 인증용 사진 촬영
    public record getCurrentFaceImgDTO(
            String imgUrl
    ) {
    }

    // 얼굴 인증
    public record identifyFaceDTO(
            String originImg,
            String currentImg
    ) {
    }
}
