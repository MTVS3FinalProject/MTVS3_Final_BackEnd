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

    public TitleAcquiredEvent(Long memberId,
                              Long titleId,
                              String titleName,
                              String titleScript,
                              String titleRarity) {
        this.memberId = memberId;
        this.titleId = titleId;
        this.titleName = titleName;
        this.titleScript = titleScript;
        this.titleRarity = titleRarity;
    }
}