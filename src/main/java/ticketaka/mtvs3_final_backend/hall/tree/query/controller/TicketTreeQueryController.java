package ticketaka.mtvs3_final_backend.hall.tree.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.hall.tree.query.service.TicketTreeQueryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hall/tree")
@Tag(name = "TicketTreeQueryController")
public class TicketTreeQueryController {

    private final TicketTreeQueryService ticketTreeQueryService;
}
