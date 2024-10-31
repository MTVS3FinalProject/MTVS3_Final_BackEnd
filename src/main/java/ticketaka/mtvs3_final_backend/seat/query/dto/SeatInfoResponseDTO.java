package ticketaka.mtvs3_final_backend.seat.query.dto;

public class SeatInfoResponseDTO {

    // 좌석 정보 조회
    public record getSeatInfoDTO(
            int floor,
            String seatInfo,
            timeDTO concertTime,
            timeDTO drawingTime,
            int competitionRate
    ) {
    }

    // 공연 날짜
    public record timeDTO(
            int year,
            int month,
            int day,
            String time
    ) {
    }
}
