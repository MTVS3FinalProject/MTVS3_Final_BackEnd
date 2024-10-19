package ticketaka.mtvs3_final_backend.seat.command.application.dto;

import java.util.List;

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

    public record getReceptionSeatsDTO(
            List<ReceptionSeatDTO> receptionSeatDTOList
    ) {
        protected record ReceptionSeatDTO(
                String seatId,
                String concertDate,
                String seatInfo,
                String drawingTime,
                int competitionRate
        ) {

        }
    }
}
