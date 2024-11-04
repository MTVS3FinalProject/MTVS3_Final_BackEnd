package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.dto;

public class SeatDTO {

    public record getSeatId(
            String section,
            String number
    ) {
    }
}
