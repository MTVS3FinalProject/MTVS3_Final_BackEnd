package ticketaka.mtvs3_final_backend.member.command.domain.model.property;

public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    public static Gender fromString(String genderStr) {
        if (genderStr == null) {
            return Gender.UNKNOWN;
        }

        return switch (genderStr.toLowerCase()) {
            case "male" -> Gender.MALE;
            case "female" -> Gender.FEMALE;
            default -> Gender.UNKNOWN;
        };
    }
}