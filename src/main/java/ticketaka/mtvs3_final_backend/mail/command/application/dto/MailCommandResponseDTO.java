package ticketaka.mtvs3_final_backend.mail.command.application.dto;

import java.util.List;

public class MailCommandResponseDTO {

    public record getMailListDTO(
            List<mailDTO> mailListDTO
    ) {
    }

    public record mailDTO(
            int mailId,
            String subject,
            String content,
            String mailCategory,
            Boolean isRead
    ) {
    }
}
