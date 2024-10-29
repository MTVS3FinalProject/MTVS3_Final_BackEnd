package ticketaka.mtvs3_final_backend.seat.command.application.controller;

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
public class SeatController {

    private final SeatService seatService;

    /*
        좌석 조회
     */
    @GetMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<?> getSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("getSeat_requestDTO : {}", requestDTO);

        SeatResponseDTO.getSeatDTO responseDTO = seatService.getSeat(requestDTO);

        log.info("getSeat_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수
     */
    @PostMapping("/{concertId}/seats/{seatId}/reception")
    public ResponseEntity<?> seatReception(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("seatReception_requestDTO : {}", requestDTO);

        SeatResponseDTO.seatReceptionDTO responseDTO = seatService.seatReception(requestDTO, getCurrentMemberId());

        log.info("seatReception_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        현재 회원이 접수한 좌석 조회
     */
    @GetMapping("/{concertId}/receptions")
    public ResponseEntity<?> getReceptionSeats(@RequestBody SeatRequestDTO.getReceptionSeatsDTO requestDTO) {

        log.info("getReceptionSeats_requestDTO : {}", requestDTO);

        SeatResponseDTO.getReceptionSeatsDTO responseDTO = seatService.getReceptionSeats(requestDTO, getCurrentMemberId());

        log.info("getReceptionSeats_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수 취소
     */
    @DeleteMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<?> cancelReceptionSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("cancelReceptionSeat_requestDTO : {}", requestDTO);

        SeatResponseDTO.cancelReceptionSeatDTO responseDTO = seatService.cancelReceptionSeat(requestDTO, getCurrentMemberId());

        log.info("cancelReceptionSeat_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        추첨 시작 알림
     */
    @PostMapping("/{concertId}/seats/{seatId}/drawing")
    public ResponseEntity<?> drawingNotification(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("drawingNotification_requestDTO : {}", requestDTO);

        SeatResponseDTO.createDrawingNotificationDTO responseDTO = seatService.createDrawingNotification(requestDTO);

        log.info("drawingNotification_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 추첨 결과 반영
     */
    @PostMapping("/{concertId}/seats/{seatId}/result")
    public ResponseEntity<?> createDrawResult(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("createDrawResult_requestDTO : {}", requestDTO);

        seatService.createDrawResult(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 추첨 결과 - 치트
     */
    @PostMapping("/{concertId}/draw-cheat")
    public ResponseEntity<?> cheatDrawResult(@RequestBody SeatRequestDTO.cheatDTO requestDTO) {

        seatService.cheatDrawResult(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제
     */
    @PostMapping("/{concertId}/seats/{seatId}/payment")
    public ResponseEntity<?> reserveSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        log.info("reserveSeat_requestDTO : {}", requestDTO);

        SeatResponseDTO.reserveSeatDTO responseDTO = seatService.reserveSeat(requestDTO, getCurrentMemberId());

        log.info("reserveSeat_responseDTO: {}", responseDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 결제 - 치트
     */
    @PostMapping("/{concertId}/payment-cheat")
    public ResponseEntity<?> cheatReserveSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.reserveSeatDTO responseDTO = seatService.cheatReserveSeat(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
