package ticketaka.mtvs3_final_backend.ticketing.concert.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertQueryService {

    private final ConcertQueryRepository concertQueryRepository;

    // ConcertList 조회
    public List<Concert> getConcertList(List<Long> concertIdList) {
        return concertQueryRepository.findAllById(concertIdList);
    }
}
