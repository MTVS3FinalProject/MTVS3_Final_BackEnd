package ticketaka.mtvs3_final_backend.mail.command.application.dto;

import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;

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

    public record readPuzzleMailDTO(
            int mailId,
            String subject,
            String content,
            String mailCategory,
            int rank,
            getMemberTitleDTO titleInfo,
            getMemberStickerDTO stickerINfo
    ) {
    }
}
