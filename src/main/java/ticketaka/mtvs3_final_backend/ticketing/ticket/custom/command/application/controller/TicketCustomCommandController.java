package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service.TicketCustomCommandService;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketCustomCommandController {

    private final TicketCustomCommandService ticketCustomCommandService;

    /*
        스티커 생성
     */
    @PostMapping("/{ticketId}/sticker")
    public ResponseEntity<?> createAISticker(@PathVariable("ticketId") Long ticketId) {

        log.info("createAISticker Request: ticketId = {}", ticketId);

        TicketCustomCommandResponseDTO.createAIStickerDTO responseDTO = ticketCustomCommandService.createAISticker(getCurrentMemberId());

        log.info("createAISticker Response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        배경 생성
     */
}
