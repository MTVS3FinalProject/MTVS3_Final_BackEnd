package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.application.dto.SeatCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.application.service.SeatCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "05_01_01_SeatController")
public class SeatCommandController {

    private final SeatCommandService seatCommandService;

    /*
        좌석 접수
     */
    @PostMapping("/{concertId}/seats/{seatId}/reception")
    public ResponseEntity<?> seatReception(@PathVariable("concertId") Long concertId,
                                           @PathVariable("seatId") Long seatId) {

        SeatCommandResponseDTO.seatReceptionDTO responseDTO = seatCommandService.seatReception(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수 취소
     */
    @DeleteMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<?> cancelReceptionSeat(@PathVariable("concertId") Long concertId,
                                                 @PathVariable("seatId") Long seatId) {

        SeatCommandResponseDTO.cancelReceptionSeatDTO responseDTO = seatCommandService.cancelReception(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        추첨 시작 알림
     */
    @PostMapping("/{concertId}/seats/{seatId}/drawing")
    public ResponseEntity<?> drawingNotification(@PathVariable("concertId") Long concertId,
                                                 @PathVariable("seatId") Long seatId) {

        SeatCommandResponseDTO.createDrawingNotificationDTO responseDTO = seatCommandService.drawingNotification(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 추첨 결과 반영
     */
    @PostMapping("/{concertId}/seats/{seatId}/result")
    public ResponseEntity<?> processDrawResult(@PathVariable("concertId") Long concertId,
                                               @PathVariable("seatId") Long seatId) {

        seatCommandService.processDrawResult(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제 미루기
     */
    @PostMapping("/{concertId}/seats/{seatId}/postpone")
    public ResponseEntity<?> postponeSeat(@PathVariable("concertId") Long concertId,
                                          @PathVariable("seatId") Long seatId) {

        seatCommandService.postponeSeat(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제
     */
    @PostMapping("/{concertId}/seats/{seatId}/payment")
    public ResponseEntity<?> reserveSeat(@PathVariable("concertId") Long concertId,
                                         @PathVariable("seatId") Long seatId) {

        SeatCommandResponseDTO.reserveSeatDTO responseDTO = seatCommandService.reserveSeat(getCurrentMemberId(), concertId, seatId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 추첨 결과 - 치트
     */
    @PostMapping("/{concertId}/draw-cheat")
    public ResponseEntity<?> cheatDrawResult(@PathVariable("concertId") Long concertId) {

        seatCommandService.cheatDrawResult(getCurrentMemberId(), concertId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제 - 치트
     */
    @PostMapping("/{concertId}/payment-cheat")
    public ResponseEntity<?> cheatReserveSeat(@PathVariable("concertId") Long concertId) {

        SeatCommandResponseDTO.reserveSeatDTO responseDTO = seatCommandService.cheatReserveSeat(getCurrentMemberId(), concertId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
