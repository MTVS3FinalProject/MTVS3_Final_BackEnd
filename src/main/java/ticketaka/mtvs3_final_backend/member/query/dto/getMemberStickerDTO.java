package ticketaka.mtvs3_final_backend.member.query.dto;

import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;

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
