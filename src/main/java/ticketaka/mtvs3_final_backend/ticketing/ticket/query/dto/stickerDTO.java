package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

public record stickerDTO(
        int stickerId,
        String stickerImage
) {
    public stickerDTO(Long stickerId, String fileUrl) {
        this(stickerId.intValue(), fileUrl);
    }
}
