package ticketaka.mtvs3_final_backend.seat.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.seat.command.application.service.SeatService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concert/seat")
public class SeatController {

    private final SeatService seatService;
}
