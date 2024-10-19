package ticketaka.mtvs3_final_backend.seat.command.application.dto;

public class SeatResponseDTO {

    public record getSeatDTO(
            String seatId,
            String seatInfo,
            String drawingTime,
            int competitionRate
    ) {
    }
}
