package ticketaka.mtvs3_final_backend.admin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminVerificationResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.application.service.AdminCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminTicketController {

    private final AdminCommandService adminCommandService;

    /*
        Ticket Verification
     */
    @GetMapping("/verification/tickets/{ticketId}")
    public ResponseEntity<?> verifyTicket(@PathVariable("ticketId") Long ticketId) {

        AdminVerificationResponseDTO.verifyTicketDTO responseDTO = adminCommandService.verifyTicket(ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        Use Ticket
     */
    @PutMapping("/consume/tickets/{ticketId}")
    public ResponseEntity<?> consumeTicket(@PathVariable("ticketId") Long ticketId) {

        adminCommandService.consumeTicket(ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
