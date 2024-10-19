package ticketaka.mtvs3_final_backend.seat.command.application.dto;

public class SeatResponseDTO {

    public record getSeatDTO(
            String seatId,
            String seatInfo,
            String drawingTime,
            int competitionRate
    ) {
    }

    public record seatReceptionDTO(
            int competitionRate,
            int seatPrice,
            int remainingTicket
    ) {
    }
}
