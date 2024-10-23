package ticketaka.mtvs3_final_backend.ticket.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.ticket.command.application.service.TicketService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concert/ticket")
public class TicketController {

    private final TicketService ticketService;
}
