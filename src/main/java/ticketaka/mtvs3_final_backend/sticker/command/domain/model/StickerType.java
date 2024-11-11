package ticketaka.mtvs3_final_backend.sticker.command.domain.model;

public enum StickerType {
    COMMON, COLLECTION, UNKNOWN;

    public static StickerType fromString(String stickerType) {

        if (stickerType == null) {
            return UNKNOWN;
        }

        return switch (stickerType.toLowerCase()) {
            case "common" -> COMMON;
            case "collection" -> COLLECTION;
            default -> UNKNOWN;
        };
    }
}
