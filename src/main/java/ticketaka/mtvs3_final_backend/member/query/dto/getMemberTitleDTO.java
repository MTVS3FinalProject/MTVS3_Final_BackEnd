package ticketaka.mtvs3_final_backend.member.query.dto;

import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

public record getMemberTitleDTO(
        int titleId,
        String titleName,
        String titleScript,
        String titleRarity,
        Boolean isRepresentative
) {
    public getMemberTitleDTO(Long titleId, String titleName, String titleScript, TitleRarity titleRarity, Boolean isRepresentative) {
        this(titleId.intValue(), titleName, titleScript, titleRarity.toString(), isRepresentative);
    }
}
