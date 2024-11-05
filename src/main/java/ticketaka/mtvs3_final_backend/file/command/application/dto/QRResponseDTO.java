package ticketaka.mtvs3_final_backend.file.command.application.dto;

public class QRResponseDTO {

    public record generateVerificationQRDTO(
            byte[] image,
            String userCode
    ) {
    }

    public record checkVerificationQR(
            int floor,
            int seatNum,
            String seatInfo
    ) {
    }
}
