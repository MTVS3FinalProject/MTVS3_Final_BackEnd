package ticketaka.mtvs3_final_backend.member.query.infrastructure.event;

import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;

public record TitleAcquiredEvent(
        Long memberId,
        Long titleId,
        String titleName,
        String titleScript,
        String titleRarity
) {
    public TitleAcquiredEvent(Long memberId, Title title) {
        this(memberId, title.getId(), title.getTitleName(), title.getTitleScript(), title.getTitleRarity().toString());
    }
}