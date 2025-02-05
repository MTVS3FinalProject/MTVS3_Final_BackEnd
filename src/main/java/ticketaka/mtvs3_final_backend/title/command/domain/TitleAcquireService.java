package ticketaka.mtvs3_final_backend.title.command.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.repository.TitleCommandRepository;
import ticketaka.mtvs3_final_backend.title.command.infrastructure.event.TitleAcquiredEvent;
import ticketaka.mtvs3_final_backend.title.command.infrastructure.event.TitleEventProducer;


@Slf4j
@RequiredArgsConstructor
@Service
public class TitleAcquireService {

    private final TitleCommandRepository titleCommandRepository;
    private final TitleEventProducer titleEventProducer;

    // Title 할당
    public Title getPuzzleResult(Long memberId, Long concertId, TitleRarity titleRarity) {

        Title title =  titleCommandRepository.getPuzzleResultByMemberIdAndConcertId(memberId, concertId, titleRarity)
                .orElse(null);

        if (title != null) {
            titleEventProducer.produceTitleAcquiredEvent(new TitleAcquiredEvent(memberId, title));
        }
        return title;
    }
}
