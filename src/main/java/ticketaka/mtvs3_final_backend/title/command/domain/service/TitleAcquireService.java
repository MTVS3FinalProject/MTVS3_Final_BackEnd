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

        return titleCommandRepository.getPuzzleResultByMemberIdAndConcertId(memberId, concertId, rarity)
                .map(title -> {
                    titleEventProducer.produceTitleAcquiredEvent(
                            new TitleAcquiredEvent(
                                    memberId,
                                    title.getId(),
                                    title.getTitleName(),
                                    title.getTitleScript(),
                                    title.getTitleRarity().toString()
                            ));
                    return title;
                });
    }
}
