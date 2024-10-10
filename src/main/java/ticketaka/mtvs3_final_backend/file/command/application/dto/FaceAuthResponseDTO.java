package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class FaceAuthResponseDTO {

    // 얼굴 인증
    public record identifyFaceDTO(
            double similarity_score,
            int match_result
    ) {
    }
}
