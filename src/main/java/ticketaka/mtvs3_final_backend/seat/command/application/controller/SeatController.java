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
@RequestMapping("/api/concert/seat")
public class SeatController {

    private final SeatService seatService;

    /*
        좌석 조회
     */
    @PostMapping
    public ResponseEntity<?> getSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.getSeatDTO responseDTO = seatService.getSeat(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수
     */
    @PostMapping("/reception")
    public ResponseEntity<?> seatReception(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.seatReceptionDTO responseDTO = seatService.seatReception(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        현재 회원이 접수한 좌석 조회
     */
    @PostMapping("/my/reception")
    public ResponseEntity<?> getReceptionSeats(@RequestBody SeatRequestDTO.getReceptionSeatsDTO requestDTO) {

        SeatResponseDTO.getReceptionSeatsDTO responseDTO = seatService.getReceptionSeats(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수 취소
     */
    @DeleteMapping("/reception")
    public ResponseEntity<?> cancelReceptionSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.cancelReceptionSeatDTO responseDTO = seatService.cancelReceptionSeat(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 게임 결과 반영
     */
    @PostMapping("/pre-reserve")
    public ResponseEntity<?> getPreReserveSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        좌석 결제
     */
    @PostMapping("/payment")
    public ResponseEntity<?> reserveSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.reserveSeatDTO responseDTO = null;

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
