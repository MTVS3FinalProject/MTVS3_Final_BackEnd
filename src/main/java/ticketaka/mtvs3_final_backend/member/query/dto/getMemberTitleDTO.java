package ticketaka.mtvs3_final_backend.member.query.dto;

import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

public record getMemberTitleDTO(
        int titleId,
        String titleName,
        String titleScript,
        String titleRarity
) {
    public getMemberTitleDTO(Long titleId, String titleName, String titleScript, String titleRarity) {
        this(titleId.intValue(), titleName, titleScript, titleRarity);
    }
}
