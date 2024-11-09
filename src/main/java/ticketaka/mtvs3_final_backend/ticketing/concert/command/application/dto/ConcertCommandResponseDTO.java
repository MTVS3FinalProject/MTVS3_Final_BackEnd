package ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto;

import java.util.List;

public class ConcertCommandResponseDTO {

    // 모든 공연 정보 조회
    public record getConcertListDTO(
            List<getConcertDTO> concertDTOList
    ) {
    }

    // 공연 정보 조회
    public record getConcertDTO(
            int concertId,
            String concertName,
            timeDTO concertTime
    ) {
    }

    // 공연장 입장
    public record entranceConcertDTO(
            int concertId,
            String concertName,
            timeDTO concertTime,
            List<SeatIdDTO> availableSeats,
            List<SeatIdDTO> receptionSeats,
            int remainingTickets
    ) {
    }

    public record SeatIdDTO(
            int seatId,
            String seatName,
            String drawingTime
    ) {
    }

    public record acquireStickerFromPuzzleResultDTO(
            int stickerId,
            String stickerName,
            String stickerScript,
            String stickerRarity,
            byte[] stickerImage
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

    // 공연 날짜
    public record timeDTO(
            int year,
            int month,
            int day,
            String time
    ) {
    }
}
