package ticketaka.mtvs3_final_backend.concert.command.application.dto;

import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConcertResponseDTO {

    public record entranceConcertDTO(
            String concertName,
            int year,
            int month,
            int day,
            String time,
            List<SeatIdDTO> availableSeats,
            List<SeatIdDTO> receptionSeats,
            int remainingTickets
    ) {
    }

    public record SeatIdDTO(
            String seatId
    ) {
    }

    public record ConcertDateTimeDTO(
            String year,
            String month,
            String day,
            String time
    ) {
        public ConcertDateTimeDTO(Concert concert) {
            this(
                    concert.getConcertDate().format(DateTimeFormatter.ofPattern("yyyy")),  // yyyy 형식의 연도
                    concert.getConcertDate().format(DateTimeFormatter.ofPattern("MM")),  // MM 형식의 월
                    concert.getConcertDate().format(DateTimeFormatter.ofPattern("dd")),  // dd 형식의 일
                    concert.getConcertDate().toLocalTime().toString()  // 시간 부분
            );
        }
    }
}
