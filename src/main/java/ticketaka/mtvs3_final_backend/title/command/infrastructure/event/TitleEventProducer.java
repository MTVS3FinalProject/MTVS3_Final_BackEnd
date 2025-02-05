package ticketaka.mtvs3_final_backend.title.command.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TitleEventProducer {

    private final KafkaTemplate<String, TitleAcquiredEvent> kafkaTemplate;

    public void produceTitleAcquiredEvent(TitleAcquiredEvent titleAcquiredEvent) {
        kafkaTemplate.send("title-acquired-event", titleAcquiredEvent);
    }
}
