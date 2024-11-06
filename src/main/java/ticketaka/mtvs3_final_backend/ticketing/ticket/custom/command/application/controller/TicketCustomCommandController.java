package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service.TicketCustomCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ticket")
public class TicketCustomCommandController {

    private final TicketCustomCommandService ticketCustomCommandService;

    /*
        커스텀 티켓 제작
     */
    @PostMapping("/concerts/{concertId}/custom")
    public ResponseEntity<?> createCustomTicket(@PathVariable String concertId) {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
