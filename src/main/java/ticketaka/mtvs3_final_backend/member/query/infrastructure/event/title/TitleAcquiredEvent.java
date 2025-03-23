package ticketaka.mtvs3_final_backend.member.query.infrastructure.event.title;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;

@Getter
@NoArgsConstructor
public class TitleAcquiredEvent {

    private Long memberId;
    private Long titleId;
    private String titleName;
    private String titleScript;
    private String titleRarity;

    public TitleAcquiredEvent(Long memberId, Title title) {
        this.memberId = memberId;
        this.titleId = title.getId();
        this.titleName = title.getTitleName();
        this.titleScript = title.getTitleScript();
        this.titleRarity = title.getTitleRarity().toString();
    }
}