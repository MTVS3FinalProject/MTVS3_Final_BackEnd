package ticketaka.mtvs3_final_backend.member.query.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Document(collection = "member_sticker")
public class MemberStickerData {

    @Id
    private String id;

    @Indexed
    private Long memberId;

    private List<Sticker> stickerList = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    public static class Sticker {
        private Long stickerId;
        private String stickerName;
        private String stickerScript;
        private String stickerRarity;
        private String stickerImage;

        @Builder
        public Sticker(Long stickerId, String stickerName, String stickerScript, String stickerRarity, String stickerImage) {
            this.stickerId = stickerId;
            this.stickerName = stickerName;
            this.stickerScript = stickerScript;
            this.stickerRarity = stickerRarity;
            this.stickerImage = stickerImage;
        }
    }

    @Builder
    public MemberStickerData(Long memberId, List<Sticker> stickerList) {
        this.memberId = memberId;
        this.stickerList = (stickerList != null) ? new ArrayList<>(stickerList) : new ArrayList<>();
    }

    public void addSticker(Sticker newSticker) {
        this.stickerList.add(newSticker);
    }
}
