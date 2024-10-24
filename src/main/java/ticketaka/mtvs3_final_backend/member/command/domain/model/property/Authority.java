package ticketaka.mtvs3_final_backend.member.command.domain.model.property;

public enum Authority {
    ADMIN, MANAGER, FAN, GUEST, NONE;

    public static Authority fromInt(int authority) {
        return switch (authority) {
            case 0 -> FAN;
            case 1 -> MANAGER;
            default -> GUEST;
        };
    }
}