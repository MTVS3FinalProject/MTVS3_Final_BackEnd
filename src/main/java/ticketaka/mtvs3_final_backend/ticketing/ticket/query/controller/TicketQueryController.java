package ticketaka.mtvs3_final_backend.ticketing.ticket.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.TicketQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.service.TicketQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/tickets")
public class TicketQueryController {

    private final TicketQueryService ticketQueryService;

    /*
        커스텀 티켓 목록 조회
     */
    @GetMapping
    public ResponseEntity<?> getCustomizableTicketList(@RequestParam(name = "status", required = false, defaultValue = "available") String status) {

        TicketQueryResponseDTO.getCustomizableTicketListDTO responseDTO = ticketQueryService.getCustomizableTicketList(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        티켓 커스텀 제작 입장 - 스티커 조회
     */
    @GetMapping("/{ticketId}/custom")
    public ResponseEntity<?> getTicketCustomObject(@PathVariable("ticketId") Long ticketId) {

        TicketQueryResponseDTO.getTicketCustomObjectDTO responseDTO = ticketQueryService.getTicketCustomObject(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        특정 티켓 조회
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getTicketDetails(@PathVariable("ticketId") Long ticketId) {

        getTicketDTO responseDTO = ticketQueryService.getTicketDetails(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
