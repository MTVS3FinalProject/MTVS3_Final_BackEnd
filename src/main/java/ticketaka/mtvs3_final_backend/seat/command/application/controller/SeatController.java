package ticketaka.mtvs3_final_backend.seat.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.service.SeatService;

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

        SeatResponseDTO.getSeatDTO responseDTO = null;

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수
     */
    @PostMapping
    public ResponseEntity<?> seatReception(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.seatReceptionDTO responseDTO = null;

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        현재 회원이 접수한 좌석 조회
     */
    @GetMapping
    public ResponseEntity<?> getReceptionSeats() {

        SeatResponseDTO.getReceptionSeatsDTO responseDTO = null;

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        좌석 접수 취소
     */
    @DeleteMapping
    public ResponseEntity<?> cancelReceptionSeat(@RequestBody SeatRequestDTO.seatIdDTO requestDTO) {

        SeatResponseDTO.cancelReceptionSeatDTO responseDTO = null;

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
