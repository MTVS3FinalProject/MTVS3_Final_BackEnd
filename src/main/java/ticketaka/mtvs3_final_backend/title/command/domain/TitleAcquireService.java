package ticketaka.mtvs3_final_backend.title.command.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.repository.TitleCommandRepository;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TitleAcquireService {

    private final TitleCommandRepository titleCommandRepository;

    // Title 할당
    public Title getPuzzleResult(Long memberId, Long concertId, TitleRarity titleRarity) {

        Title title =  titleCommandRepository.getPuzzleResultByMemberIdAndConcertId(memberId, concertId, titleRarity)
                .orElse(null);

        return title;
    }
}
