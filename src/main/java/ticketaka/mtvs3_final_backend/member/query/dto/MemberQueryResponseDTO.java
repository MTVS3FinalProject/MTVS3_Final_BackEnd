package ticketaka.mtvs3_final_backend.member.query.dto;

import java.util.List;

public class MemberQueryResponseDTO {

    public record getMemberInventoryDTO(
            // Title List
            List<getMemberTitleDTO> memberTitleDTOList
            // Sticker List
            // CustomTicket List
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
}
