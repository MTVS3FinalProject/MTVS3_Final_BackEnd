package ticketaka.mtvs3_final_backend.title.command.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.member.query.infrastructure.event.TitleAcquiredEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class TitleEventProducer {

    private final KafkaTemplate<String, TitleAcquiredEvent> kafkaTemplate;

    public void produceTitleAcquiredEvent(TitleAcquiredEvent titleAcquiredEvent) {

        String messageKey = titleAcquiredEvent.getMemberId().toString();
        kafkaTemplate.send("title-acquired-event", messageKey, titleAcquiredEvent);

        log.info("Produced Kafka event: key={}, event={}", messageKey, titleAcquiredEvent);
    }
}
