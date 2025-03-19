package ticketaka.mtvs3_final_backend.member.query.dto;

public record getMemberTitleDTO(
        int titleId,
        String titleName,
        String titleScript,
        String titleRarity,
        Boolean isRepresentative
) {
    public getMemberTitleDTO(Long titleId, String titleName, String titleScript, String titleRarity, Boolean isRepresentative) {
        this(titleId.intValue(), titleName, titleScript, titleRarity, isRepresentative);
    }
}
