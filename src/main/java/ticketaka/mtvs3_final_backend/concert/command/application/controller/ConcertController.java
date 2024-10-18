package ticketaka.mtvs3_final_backend.concert.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.concert.command.application.service.ConcertService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;
}
