package ticketaka.mtvs3_final_backend.concert.command.application.dto;

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
        protected record SeatIdDTO(
                String seatId
        ) {
        }
    }
}
