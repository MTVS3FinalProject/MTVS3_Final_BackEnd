package ticketaka.mtvs3_final_backend.mail.command.application.dto;

public class MailCommandResponseDTO {

    public record getMailListDTO(
            int mailId,
            String subject,
            String content,
            String mailCategory,
            Boolean isRead
    ) {
    }
}
