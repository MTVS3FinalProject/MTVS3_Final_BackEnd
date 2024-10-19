package ticketaka.mtvs3_final_backend.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;

    /*
        좌석 조회
     */
    public SeatResponseDTO.getSeatDTO getSeat(SeatRequestDTO.seatIdDTO requestDTO) {

        Concert concert = concertRepository.findByName(requestDTO.concertName())
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));

        String section = requestDTO.seatId().substring(4, 5);
        String number = requestDTO.seatId().substring(5);

        Seat seat = seatRepository.findByConcertAndSectionAndNumber(concert, section, number)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));

        // 현재 좌석에 접수한 총 인원 조회
        long count = 1;
        // count 값을 int로 변환
        int receptionCount = (int) count;
        // 경쟁률 계산 (1 / 현재 접수한 인원)
        int competitionRate = receptionCount > 0 ? 1 / receptionCount : 0;

        return new SeatResponseDTO.getSeatDTO(
                requestDTO.seatId(),
                section + number,
                seat.getDrawingTime().toString(),
                competitionRate
        );
    }
}
