package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto.TicketCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.service.TicketCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/tickets")
@Tag(name = "08_TicketCommandController")
public class TicketCommandController {

    private final TicketCommandService ticketCommandService;

    /*
        커스텀 티켓 저장
     */
    @PostMapping("/{ticketId}/custom")
    public ResponseEntity<?> saveCustomTicket(@PathVariable("ticketId") Long ticketId,
                                              @RequestBody TicketCommandRequestDTO.saveCustomTicketDTO requestDTO) {

        log.info("saveCustomTicket_requestDTO - stickerIdList: {}, backgroundId: {}", requestDTO.stickerIdList(), requestDTO.backgroundId());

        ticketCommandService.saveCustomTicket(getCurrentMemberId(), ticketId, requestDTO);

        log.info("saveCustomTicket_responseDTO : {}", requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
