package ticketaka.mtvs3_final_backend.seat.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.seat.query.dto.SeatQueryResponseDTO;
import ticketaka.mtvs3_final_backend.seat.query.service.SeatQueryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "05_02_SeatQueryController")
public class SeatQueryController {

    private final SeatQueryService seatQueryService;

    /*
        좌석 조회
     */
    @GetMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<?> GetConcertSeat(@PathVariable("concertId") int concertId,
                                            @PathVariable("seatId") int seatId) {

        log.info("getSeat_request: concertId={}, seatId={}", concertId, seatId);

        SeatQueryResponseDTO.getSeatInfoDTO responseDTO = seatQueryService.GetSeatInfo((long) concertId, (long) seatId);

        log.info("getSeat_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
