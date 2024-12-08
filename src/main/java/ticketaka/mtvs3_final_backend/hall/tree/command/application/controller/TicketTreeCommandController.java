package ticketaka.mtvs3_final_backend.hall.tree.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.hall.tree.command.application.dto.TicketTreeCommandResponseDTO;
import ticketaka.mtvs3_final_backend.hall.tree.command.application.service.TicketTreeCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hall/tree")
@Tag(name = "TicketTreeCommandController")
public class TicketTreeCommandController {

    private final TicketTreeCommandService ticketTreeCommandService;

    /*
        티켓 걸기
     */
    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<?> registerTicketTree(@PathVariable("ticketId") Long ticketId) {

        TicketTreeCommandResponseDTO.registerTicketTreeDTO responseDTO = ticketTreeCommandService.registerTicketTree(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        티켓 회수
     */

    /*
        트리 초기화
     */
    @DeleteMapping
    public ResponseEntity<?> resetTicketTree() {

        ticketTreeCommandService.resetTicketTree();

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
