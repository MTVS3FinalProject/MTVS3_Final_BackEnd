package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.service.TicketCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
public class TicketCommandController {

    private final TicketCommandService ticketCommandService;
    
    /*
        티켓 발급
     */
}
