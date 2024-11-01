package ticketaka.mtvs3_final_backend.ticketing.seat.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.dto.SeatQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.service.SeatQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

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
    public ResponseEntity<?> getConcertSeat(@PathVariable("concertId") int concertId,
                                            @PathVariable("seatId") int seatId) {

        log.info("GetConcertSeat_request: concertId={}, seatId={}", concertId, seatId);

        SeatQueryResponseDTO.getSeatInfoDTO responseDTO = seatQueryService.getConcertSeat((long) concertId, (long) seatId);

        log.info("GetConcertSeat_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        현재 회원이 접수한 좌석 조회
     */
    @GetMapping("{concertId}/receptions")
    public ResponseEntity<?> getMyConcertReceptions(@PathVariable("concertId") long concertId) {

        log.info("GetMyConcertReceptions_request: concertId={}", concertId);

        SeatQueryResponseDTO.getMyConcertReceptionsDTO responseDTO = seatQueryService.getMyConcertReceptions(
                concertId, getCurrentMemberId()
        );

        log.info("GetMyConcertReceptions_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
