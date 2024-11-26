package ticketaka.mtvs3_final_backend.admin.command.application.dto;

import java.time.LocalDateTime;

public class AdminVerificationResponseDTO {

    public record verifyTicketDTO(
            String concertName,
            LocalDateTime concertDate,
            String seatInfo
    ) {
    }
}
