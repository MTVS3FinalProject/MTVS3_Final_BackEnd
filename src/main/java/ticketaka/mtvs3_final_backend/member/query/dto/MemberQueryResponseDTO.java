package ticketaka.mtvs3_final_backend.member.query.dto;

import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

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
        public getMemberTitleDTO(Long titleId, String titleName, String titleScript, TitleRarity titleRarity, Boolean isRepresentative) {
            this(titleId.intValue(), titleName, titleScript, titleRarity.toString(), isRepresentative);
        }
    }

    // 보유 스티커 조회
    public record getMemberStickerDTO(
            int stickerId,
            String stickerName,
            String stickerScript,
            String stickerRarity,
            String stickerImage
    ) {
        public getMemberStickerDTO(Long stickerId, String stickerName, String stickerScript, StickerRarity stickerRarity, String fileUrl) {
            this(stickerId.intValue(), stickerName, stickerScript, stickerRarity.toString(), fileUrl);
        }
    }

    // 보유 티켓 이미지 조회
    public record getMemberTicketDTO(
            int ticketId,
            String concertName,
            String seatInfo,
            String ticketImage
    ) {
        public getMemberTicketDTO(Long ticketId, String concertName, String seatInfo, String ticketImage) {
            this(ticketId.intValue(), concertName, seatInfo, ticketImage);
        }
    }
}
