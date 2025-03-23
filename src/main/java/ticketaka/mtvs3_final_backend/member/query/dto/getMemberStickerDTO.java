package ticketaka.mtvs3_final_backend.member.query.dto;

public record getMemberStickerDTO(
        int stickerId,
        String stickerName,
        String stickerScript,
        String stickerRarity,
        String stickerImage
) {
    public getMemberStickerDTO(Long stickerId, String stickerName, String stickerScript, String stickerRarity, String fileUrl) {
        this(stickerId.intValue(), stickerName, stickerScript, stickerRarity, fileUrl);
    }
}
