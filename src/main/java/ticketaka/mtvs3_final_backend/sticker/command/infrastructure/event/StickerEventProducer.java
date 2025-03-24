package ticketaka.mtvs3_final_backend.sticker.command.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.member.query.infrastructure.event.sticker.StickerAcquiredEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StickerEventProducer {

    private final KafkaTemplate<String, StickerAcquiredEvent> kafkaTemplate;

    public void produceStickerAcquiredEvent(StickerAcquiredEvent stickerAcquiredEvent) {

        String messageKey = stickerAcquiredEvent.getMemberId().toString();
        kafkaTemplate.send("sticker-acquired-event", messageKey, stickerAcquiredEvent);

        log.info("Produced StickerAcquiredEvent: key={}, event={}", messageKey, stickerAcquiredEvent);
    }
}
