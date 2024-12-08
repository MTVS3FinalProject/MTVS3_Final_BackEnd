package ticketaka.mtvs3_final_backend.sticker.command.domain.model;

import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

public enum StickerRarity {
    COMMON, RARE, UNIQUE;

    public static StickerRarity fromString(String stickerRarity) {

        if (stickerRarity == null) {
            return COMMON;
        }

        return switch (stickerRarity.toLowerCase()) {
            case "rare" -> RARE;
            case "unique" -> UNIQUE;
            default -> COMMON;
        };
    }

    public static StickerRarity fromInt(int rank) {

        StickerRarity[] stickerRarities = StickerRarity.values();

        // rank 유효성 검사
        if (rank < 1 || rank > stickerRarities.length) {
            throw new Exception400("아쉽게도 Sticker 를 획득하지 못하였습니다.");
        }

        return stickerRarities[stickerRarities.length - rank];
    }

    public StickerRarity getLowerRarity() {

        int currentOrdinal = this.ordinal();

        if (currentOrdinal == 0) {
            return null;
        }

        return StickerRarity.values()[currentOrdinal - 1];
    }
}
