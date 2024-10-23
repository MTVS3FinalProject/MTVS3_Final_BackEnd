package ticketaka.mtvs3_final_backend.concert.command.application.dto;

import java.util.List;

public class ConcertResponseDTO {

    // 공연 정보 조회
    public record getConcertListDTO(
            List<getConcertDTO> concertDTOList
    ) {
    }

    public record getConcertDTO(
            String concertName,
            int year,
            int month,
            int day,
            String time
    ) {
    }

    // 공연장 입장
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
            String seatId,
            String drawingTime
    ) {
    }

    // 예매자 정보 입력
    public record enterDeliveryAddressDTO(
            String seatInfo,
            int seatNum,
            int seatPrice,
            int userCoin,
            int neededCoin
    ) {
    }
}
