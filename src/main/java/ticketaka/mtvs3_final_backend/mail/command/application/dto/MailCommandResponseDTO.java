package ticketaka.mtvs3_final_backend.mail.command.application.dto;

import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;

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
            getTitleDTO titleInfo,
            getMemberStickerDTO stickerINfo
    ) {
    }

    public record getTitleDTO(
            int titleId,
            String titleName,
            String titleScript,
            String titleRarity
    ) {
    }
}
