package ticketaka.mtvs3_final_backend.title.command.domain.model;

public enum TitleType {
    GENERAL, CONCERT, UNKNOWN;

    public static TitleType fromString(String titleType) {

        if (titleType == null) {
            return UNKNOWN;
        }

        return switch (titleType.toLowerCase()) {
            case "general" -> GENERAL;
            case "concert" -> CONCERT;
            default -> UNKNOWN;
        };
    }
}
