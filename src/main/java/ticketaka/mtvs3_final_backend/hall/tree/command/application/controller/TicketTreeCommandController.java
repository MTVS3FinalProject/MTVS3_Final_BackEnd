package ticketaka.mtvs3_final_backend.hall.tree.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
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
    public ResponseEntity<?> registerTicketTree(@PathVariable Long ticketId) {

        ticketTreeCommandService.registerTicketTree(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        티켓 회수
     */
}
