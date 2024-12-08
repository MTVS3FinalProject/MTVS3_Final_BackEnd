package ticketaka.mtvs3_final_backend.staff.concert.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.repository.ConcertRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class StaffConcertCommandService {

    private final ConcertRepository concertRepository;
}
