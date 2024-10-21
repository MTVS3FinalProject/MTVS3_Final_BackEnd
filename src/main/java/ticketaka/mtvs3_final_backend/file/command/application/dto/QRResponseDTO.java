package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class QRResponseDTO {

    public record generateSignUpQRDTO(
            byte[] image,
            String userCode
    ) {
    }
}
