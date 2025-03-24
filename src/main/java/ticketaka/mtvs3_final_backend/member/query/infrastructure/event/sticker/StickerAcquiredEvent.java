package ticketaka.mtvs3_final_backend.member.query.infrastructure.event.sticker;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StickerAcquiredEvent {

    private Long memberId;
    private Long stickerId;
    private String stickerName;
    private String stickerScript;
    private String stickerRarity;
    private String stickerImage;

    public StickerAcquiredEvent(Long memberId,
                                Long stickerId,
                                String stickerName,
                                String stickerScript,
                                String stickerRarity,
                                String stickerImage) {
        this.memberId = memberId;
        this.stickerId = stickerId;
        this.stickerName = stickerName;
        this.stickerScript = stickerScript;
        this.stickerRarity = stickerRarity;
        this.stickerImage = stickerImage;
    }
}
