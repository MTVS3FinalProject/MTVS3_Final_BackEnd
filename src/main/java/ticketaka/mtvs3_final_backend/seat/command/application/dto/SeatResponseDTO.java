package ticketaka.mtvs3_final_backend.seat.command.application.dto;

import java.util.List;

public class SeatResponseDTO {

    // 공연 날짜
    public record concertTimeDTO(
            int year,
            int month,
            int day,
            String time
    ) {
    }

    // 좌석 조회
    public record getSeatDTO(
            int year,
            int month,
            int day,
            String time,
            String seatId,
            String seatInfo,
            String drawingTime,
            int competitionRate
    ) {
    }

    // 좌석 접수
    public record seatReceptionDTO(
            int competitionRate,
            int seatPrice,
            int remainingTicket
    ) {
    }

    // 현재 회원이 접수한 좌석 조회
    public record getReceptionSeatsDTO(
            List<ReceptionSeatDTO> receptionSeatDTOList
    ) {
        public record ReceptionSeatDTO(
                String seatId,
                String concertDate,
                String seatInfo,
                String drawingTime,
                int competitionRate
        ) {
        }
    }

    // 좌석 접수 취소
    public record cancelReceptionSeatDTO(
            int remainingTicket
    ) {
    }

    // 좌석 결제
    public record reserveSeatDTO(
            String seatInfo,
            int seatPrice,
            int userCoin,
            String userName,
            String userAddress
    ) {
    }

    // 좌석 추첨 알림
    public record drawingNotificationDTO(
            List<String> nicknameList
    ) {
    }
}
