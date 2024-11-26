package ticketaka.mtvs3_final_backend.admin.command.application.dto;

public class AdminVerificationResponseDTO {

    public record verifyTicketDTO(
            String concertName,
            int year,
            int month,
            int day,
            String time,
            String seatInfo
    ) {
    }
}
