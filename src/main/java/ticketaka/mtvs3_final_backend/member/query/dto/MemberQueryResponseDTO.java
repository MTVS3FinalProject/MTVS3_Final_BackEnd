package ticketaka.mtvs3_final_backend.member.query.dto;

import java.util.List;

public class MemberQueryResponseDTO {

    /*
        최근 배송지 조회
     */
    public record getRecentMemberAddressDTO(
            String userName,
            String userPhoneNumber,
            String userAddress1,
            String userAddress2
    ) {
    }

    /*
        인벤토리 조회
     */
    public record getMemberInventoryDTO(
            // Title List
            List<getMemberTitleDTO> memberTitleDTOList,
            // Sticker List
            List<getMemberStickerDTO> memberStickerDTOList,
            // CustomTicket List
            List<getMemberTicketDTO> memberTicketDTOList
    ) {
    }
}
