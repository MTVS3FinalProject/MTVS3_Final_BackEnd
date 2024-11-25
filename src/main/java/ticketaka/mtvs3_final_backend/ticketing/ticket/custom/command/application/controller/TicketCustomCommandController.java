package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service.TicketCustomCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/tickets")
public class TicketCustomCommandController {

    private final TicketCustomCommandService ticketCustomCommandService;

    /*
        배경 생성
     */
    @PostMapping("/{ticketId}/background")
    public ResponseEntity<?> generateAIBackground(@PathVariable("ticketId") Long ticketId) {

        TicketCustomCommandResponseDTO.generateAIBackgroundDTO responseDTO = ticketCustomCommandService.generateAIBackground(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
