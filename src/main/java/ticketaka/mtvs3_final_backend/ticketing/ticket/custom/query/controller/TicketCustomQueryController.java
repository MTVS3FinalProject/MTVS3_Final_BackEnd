package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.service.TicketCustomQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/custom")
public class TicketCustomQueryController {

    private final TicketCustomQueryService ticketCustomQueryService;

    /*
        커스텀 티켓 목록 조회
     */
    @GetMapping("/tickets")
    public ResponseEntity<?> getCustomizableTicketList() {

        log.info("getCustomizableTicketList Request");

        TicketCustomQueryResponseDTO.getCustomizableTicketListDTO responseDTO = ticketCustomQueryService.getCustomizableTicketList(getCurrentMemberId());

        log.info("getCustomizableTicketList Response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        티켓 커스텀 제작 입장 - 스티커 조회
     */
    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<?> getTicketCustomInfo(@PathVariable("ticketId") Long ticketId) {

        log.info("getTicketCustomInfo Request");

        TicketCustomQueryResponseDTO.getTicketCustomInfoDTO responseDTO = ticketCustomQueryService.getTicketCustomInfo(getCurrentMemberId(), ticketId);

        log.info("getTicketCustomInfo Response : {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
