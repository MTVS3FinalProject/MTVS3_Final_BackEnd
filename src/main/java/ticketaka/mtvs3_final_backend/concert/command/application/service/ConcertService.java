package ticketaka.mtvs3_final_backend.concert.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertRequestDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertResponseDTO;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    /*
        공연장 입장
     */
    public ConcertResponseDTO.entranceConcertDTO entranceConcert(ConcertRequestDTO.entranceConcertDTO requestDTO, Long currentMemberId) {

        Concert concert = concertRepository.findByName(requestDTO.concertName())
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));

        List<Seat> availableSeatList = seatRepository.findByConcertAndSeatStatus(concert, SeatStatus.AVAILABLE);
        List<ConcertResponseDTO.SeatIdDTO> availableSeats = availableSeatList.stream()
                .map(seat -> {
                    String year = String.valueOf(concert.getConcertDate().getYear());
                    String seatId = year + seat.getSection() + seat.getNumber();

                    return new ConcertResponseDTO.SeatIdDTO(seatId);
                })
                .toList();

        // 내가 접수한 좌석 조회
        List<Seat> receptionSeatList = null;
        List<ConcertResponseDTO.SeatIdDTO> receptionSeats = null;

        int remainingTickets = concert.getReceptionLimit() - receptionSeats.size();

        return new ConcertResponseDTO.entranceConcertDTO(
                concert.getName(),
                concert.getConcertDate().getYear(),
                concert.getConcertDate().getMonthValue(),
                concert.getConcertDate().getDayOfMonth(),
                concert.getConcertDate().toLocalTime().toString(),
                availableSeats,
                receptionSeats,
                remainingTickets
        );
    }
}
