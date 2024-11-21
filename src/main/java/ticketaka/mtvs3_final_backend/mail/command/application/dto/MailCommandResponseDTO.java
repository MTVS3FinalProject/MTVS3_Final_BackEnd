package ticketaka.mtvs3_final_backend.mail.command.application.dto;

public class MailCommandResponseDTO {

    public record readMailDTO(
            int mailId,
            String subject,
            String content,
            String mailCategory
    ) {
    }

    public record readPostponeMailDTO(
            int concertId,
            int seatId
    ) {
    }
}
