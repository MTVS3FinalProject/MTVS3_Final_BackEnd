package ticketaka.mtvs3_final_backend.sticker.command.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.member.query.infrastructure.event.sticker.StickerAcquiredEvent;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.command.domain.repository.StickerCommandRepository;
import ticketaka.mtvs3_final_backend.sticker.command.infrastructure.event.StickerEventProducer;

@Slf4j
@RequiredArgsConstructor
@Service
public class StickerAcquireService {

    private final StickerCommandRepository stickerCommandRepository;
    private final StickerEventProducer stickerEventProducer;

    // Sticker 할당
    public Sticker getStickerByPuzzleResult(Long memberId, Long concertId, int rank) {

        Sticker sticker = stickerCommandRepository.getPuzzleResultByMemberIdAndConcertId(memberId, concertId, StickerRarity.fromInt(rank))
                .orElse(null);

        if (sticker != null) {
            String stickerImageUrl = "";
            stickerEventProducer.produceStickerAcquiredEvent(
                    new StickerAcquiredEvent(
                            memberId,
                            sticker.getId(),
                            sticker.getStickerName(),
                            sticker.getStickerScript(),
                            sticker.getStickerRarity().toString(),
                            stickerImageUrl
                    ));
        }
        return sticker;
    }
}
