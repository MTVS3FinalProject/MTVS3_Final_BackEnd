package ticketaka.mtvs3_final_backend.member.command.domain.model.property;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum AgeGroup {
    ZERO_TO_NINE,
    TEN_TO_NINETEEN,
    TWENTY_TO_TWENTYNINE,
    THIRTY_TO_THIRTYNINE,
    FORTY_TO_FORTYNINE,
    FIFTY_TO_FIFTYNINE,
    SIXTY_ABOVE,
    UNKNOWN;

    public static AgeGroup fromString(String age_range) {
        log.info("AgeGroup : {}", age_range);

        // age_range 가 null 이거나 빈 문자열인 경우 UNKNOWN 반환
        if (age_range == null || age_range.isEmpty()) {
            return AgeGroup.UNKNOWN;
        }

        return switch (age_range) {
            case "1~9" -> AgeGroup.ZERO_TO_NINE;
            case "10~14", "15~19", "10~19" -> AgeGroup.TEN_TO_NINETEEN;
            case "20~29" -> AgeGroup.TWENTY_TO_TWENTYNINE;
            case "30~39" -> AgeGroup.THIRTY_TO_THIRTYNINE;
            case "40~49" -> AgeGroup.FORTY_TO_FORTYNINE;
            case "50~59" -> AgeGroup.FIFTY_TO_FIFTYNINE;
            case "60~69", "70~79", "80~89", "90~" -> AgeGroup.SIXTY_ABOVE;
            default -> AgeGroup.UNKNOWN;
        };
    }
}