package ticketaka.mtvs3_final_backend.mail.query.dto;

import java.util.List;

public class MailQueryResponseDTO {

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
