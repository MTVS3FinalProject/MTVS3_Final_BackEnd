package ticketaka.mtvs3_final_backend.member.query.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.service.MemberTitleAcquiredService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TitleEventConsumer {

    private final MemberTitleAcquiredService memberTitleAcquiredService;

    @KafkaListener(topics = "title-acquired-event", groupId = "title-query-group")
    @Transactional
    public void consumeTitleAcquiredEvent(TitleAcquiredEvent event) {
        memberTitleAcquiredService.handleTitleAcquired(
                event.memberId(), event.titleId(), event.titleName(), event.titleScript(), event.titleRarity()
        );
    }
}
