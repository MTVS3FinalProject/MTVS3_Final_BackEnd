package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class QRRequestDTO {

    public record generateQRDTO(
            String email
    ) {
    }

    public record checkVerificationQRDTO(
            String userCode
    ) {
    }
}
