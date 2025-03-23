package ticketaka.mtvs3_final_backend.member.query.infrastructure.event.title;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberTitleDataRepository;
import ticketaka.mtvs3_final_backend.member.query.service.MemberTitleAcquiredService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TitleEventConsumer {

    private final MemberTitleAcquiredService memberTitleAcquiredService;
    private final MemberTitleDataRepository memberTitleDataRepository;

    @KafkaListener(topics = "title-acquired-event", groupId = "title-query-group")
    @Transactional
    public void consumeTitleAcquiredEvent(TitleAcquiredEvent event) {

        log.info("Consume Kafka event");

        boolean exists = memberTitleDataRepository.existsByMemberIdAndTitleList_TitleId(event.getMemberId(), event.getTitleId());

        if (exists) {
            log.info("이미 존재하는 타이틀 이벤트 무시: memberId={}, titleId={}", event.getMemberId(), event.getTitleId());
            return;
        }

        memberTitleAcquiredService.handleTitleAcquired(
                event.getMemberId(), event.getTitleId(), event.getTitleName(), event.getTitleScript(), event.getTitleRarity()
        );
    }
}
