package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class QRRequestDTO {

    public record generateSignUpQRDTO(
            String email
    ) {
    }
}
