package ticketaka.mtvs3_final_backend.title.command.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.member.query.infrastructure.event.title.TitleAcquiredEvent;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.repository.TitleCommandRepository;
import ticketaka.mtvs3_final_backend.title.command.infrastructure.event.TitleEventProducer;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class TitleAcquireService {

    private final TitleCommandRepository titleCommandRepository;
    private final TitleEventProducer titleEventProducer;

    // Title 할당
    public Optional<Title> getTitleByPuzzleResult(Long memberId, Long concertId, int rank) {
        TitleRarity rarity = TitleRarity.fromInt(rank);

        Optional<Title> optionalTitle = getAvailableTitle(memberId, concertId, rarity);

        if (optionalTitle.isEmpty()) {
            TitleRarity lowerRarity = rarity.getLowerRarity();
            if (lowerRarity != null) {
                optionalTitle = getAvailableTitle(memberId, concertId, lowerRarity);
            }
        }

        optionalTitle.ifPresent(title -> publishTitleAcquiredEvent(memberId, title));

        return optionalTitle;
    }

    private Optional<Title> getAvailableTitle(Long memberId, Long concertId, TitleRarity rarity) {
        return titleCommandRepository.getPuzzleResultByMemberIdAndConcertId(memberId, concertId, rarity);
    }

    private void publishTitleAcquiredEvent(Long memberId, Title title) {
        titleEventProducer.produceTitleAcquiredEvent(
                new TitleAcquiredEvent(
                        memberId,
                        title.getId(),
                        title.getTitleName(),
                        title.getTitleScript(),
                        title.getTitleRarity().toString()
                ));
    }
}
