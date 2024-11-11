package ticketaka.mtvs3_final_backend.ticketing.ticket.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.TicketQueryResponseDTO;
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
    public ResponseEntity<?> getCustomizableTicketList() {

        log.info("getCustomizableTicketList Request");

        TicketQueryResponseDTO.getCustomizableTicketListDTO responseDTO = ticketQueryService.getCustomizableTicketList(getCurrentMemberId());

        log.info("getCustomizableTicketList Response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
