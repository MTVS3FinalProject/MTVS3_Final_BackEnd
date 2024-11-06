package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketCustomQueryController {

    /*
        티켓 커스텀 가능한 공연 리스트 조회
     */
    @GetMapping("/concerts")
    public ResponseEntity<?> getConcertForCustomTicket() {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        티켓 커스텀 제작 입장
     */
    @GetMapping("/{ticketId}/custom")
    public ResponseEntity<?> getTicketCustomInfo(@PathVariable("ticketId") String ticketId) {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
