package ticketaka.mtvs3_final_backend.title.command.domain.model;

import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;

public enum TitleRarity {
    COMMON, RARE, UNIQUE;

    public static TitleRarity fromString(String titleRarity) {

        if (titleRarity == null) {
            return COMMON;
        }

        return switch (titleRarity.toLowerCase()) {
            case "rare" -> RARE;
            case "unique" -> UNIQUE;
            default -> COMMON;
        };
    }

    public static TitleRarity fromInt(int rank) {

        TitleRarity[] titleRarities = TitleRarity.values();

        // rank 유효성 검사
        if (rank < 1 || rank > titleRarities.length) {
            throw new Exception400("아쉽게도 Title 을 획득하지 못하였습니다.");
        }

        return titleRarities[titleRarities.length - rank];
    }

    public TitleRarity getLowerRarity() {

        int currentOrdinal = this.ordinal();

        if (currentOrdinal == 0) {
            return null;
        }

        return TitleRarity.values()[currentOrdinal - 1];
    }
}
