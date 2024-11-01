package ticketaka.mtvs3_final_backend.seat.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.service.SeatService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "05_01_01_SeatController")
public class SeatController {

    private final SeatService seatService;

    /*
        좌석 접수
     */
    @PostMapping("/{concertId}/seats/{seatId}/reception")
    public ResponseEntity<?> seatReception(@PathVariable("concertId") int concertId,
                                           @PathVariable("seatId") int seatId) {

        log.info("seatReception_request: concertId={}, seatId={}", concertId, seatId);

        SeatResponseDTO.seatReceptionDTO responseDTO = seatService.seatReception((long) concertId, (long) seatId, getCurrentMemberId());

        log.info("seatReception_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        현재 회원이 접수한 좌석 조회
     */
    @GetMapping("/{concertId}/receptions")
    public ResponseEntity<?> getReceptionSeats(@PathVariable("concertId") int concertId) {

        log.info("getReceptionSeats_request: concertId={}", concertId);

        SeatResponseDTO.getReceptionSeatsDTO responseDTO = seatService.getReceptionSeats((long) concertId, getCurrentMemberId());

        log.info("getReceptionSeats_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수 취소
     */
    @DeleteMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<?> cancelReceptionSeat(@PathVariable("concertId") int concertId,
                                                 @PathVariable("seatId") int seatId) {

        log.info("cancelReceptionSeat_request: concertId={}, seatId={}", concertId, seatId);

        SeatResponseDTO.cancelReceptionSeatDTO responseDTO = seatService.cancelReceptionSeat((long) concertId, (long) seatId, getCurrentMemberId());

        log.info("cancelReceptionSeat_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        추첨 시작 알림
     */
    @PostMapping("/{concertId}/seats/{seatId}/drawing")
    public ResponseEntity<?> drawingNotification(@PathVariable("concertId") int concertId,
                                                 @PathVariable("seatId") int seatId) {

        log.info("drawingNotification_request: concertId={}, seatId={}", concertId, seatId);

        SeatResponseDTO.createDrawingNotificationDTO responseDTO = seatService.createDrawingNotification((long) concertId, (long) seatId);

        log.info("drawingNotification_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 추첨 결과 반영
     */
    @PostMapping("/{concertId}/seats/{seatId}/result")
    public ResponseEntity<?> processDrawResult(@PathVariable("concertId") int concertId,
                                               @PathVariable("seatId") int seatId) {

        log.info("createDrawResult_request: concertId={}, seatId={}", concertId, seatId);

        seatService.processDrawResult((long) concertId, (long) seatId, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 추첨 결과 - 치트
     */
    @PostMapping("/{concertId}/draw-cheat")
    public ResponseEntity<?> cheatDrawResult(@PathVariable("concertId") int concertId) {

        seatService.cheatDrawResult((long) concertId, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제
     */
    @PostMapping("/{concertId}/seats/{seatId}/payment")
    public ResponseEntity<?> reserveSeat(@PathVariable("concertId") int concertId,
                                         @PathVariable("seatId") int seatId) {

        log.info("reserveSeat_request: concertId={}, seatId={}", concertId, seatId);

        SeatResponseDTO.reserveSeatDTO responseDTO = seatService.reserveSeat((long) concertId, (long) seatId, getCurrentMemberId());

        log.info("reserveSeat_response: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 결제 - 치트
     */
    @PostMapping("/{concertId}/payment-cheat")
    public ResponseEntity<?> cheatReserveSeat(@PathVariable("concertId") int concertId) {

        SeatResponseDTO.reserveSeatDTO responseDTO = seatService.cheatReserveSeat((long) concertId, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
