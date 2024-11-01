package ticketaka.mtvs3_final_backend.ticketing.seat.query.dto;

import ticketaka.mtvs3_final_backend.ticketing.seat.command.application.dto.SeatResponseDTO;

import java.util.List;

public class SeatQueryResponseDTO {

    // 공연 날짜
    public record timeDTO(
            int year,
            int month,
            int day,
            String time
    ) {
    }

    // 좌석 정보 조회
    public record getSeatInfoDTO(
            int floor,
            String seatInfo,
            boolean isReceived,
            timeDTO concertTime,
            timeDTO drawingTime,
            String seatStatus,
            Integer competitionRate
    ) {
    }

    // 좌석 정보
    public record receptionSeatDTO(
            int seatId,
            String seatName,
            String seatInfo,
            timeDTO concertTime,
            timeDTO drawingTime,
            int competitionRate
    ) {
    }

    // 현재 회원이 접수한 좌석 조회
    public record getMyConcertReceptionsDTO(
            List<receptionSeatDTO> receptionSeatDTOList
    ) {
    }
}
