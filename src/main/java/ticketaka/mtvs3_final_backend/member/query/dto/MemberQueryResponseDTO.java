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

    // 보유 칭호 조회
    public record getMemberTitleDTO(
            int titleId,
            String titleName,
            String titleScript,
            String titleRarity,
            Boolean isRepresentative
    ) {
    }

    // 보유 스티커 조회
    public record getMemberStickerDTO(
            int stickerId,
            String stickerName,
            String stickerScript,
            String stickerRarity,
            byte[] stickerImage
    ) {
    }

    // 보유 티켓 이미지 조회
    public record getMemberTicketDTO(
            int ticketId,
            String concertName,
            String seatInfo,
            byte[] ticketImage
    ) {
    }
}
