package ticketaka.mtvs3_final_backend.sticker.command.domain.model;

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
}
