package ticketaka.mtvs3_final_backend.member.query.dto;

import java.util.List;

public class MemberQueryResponseDTO {

    public record getMemberInventoryDTO(
            // Title List
            List<getMemberTitleDTO> memberTitleDTOList,
            // Sticker List
            List<getMemberStickerDTO> memberStickerDTOList,
            // CustomTicket List
            List<getMemberTicketDTO> memberTicketDTOList
    ) {
    }

    public record getMemberTitleDTO(
            int titleId,
            String titleName,
            String titleScript,
            String titleRarity,
            Boolean isRepresentative
    ) {
    }

    public record getMemberStickerDTO(
            int stickerId,
            String stickerName,
            String stickerScript,
            String stickerRarity,
            byte[] stickerImage
    ) {
    }

    public record getMemberTicketDTO(
            int ticketId,
            String concertName,
            String seatInfo,
            byte[] ticketImage
    ) {
    }
}
