package ticketaka.mtvs3_final_backend.seat.command.application.dto;

public class SeatRequestDTO {

    public record seatIdDTO(
            String concertName,
            String seatId
    ) {
    }

    public record getReceptionSeatsDTO(
            String concertName
    ) {
    }

    public record cheatDTO(
            String concertName
    ) {
    }
}
