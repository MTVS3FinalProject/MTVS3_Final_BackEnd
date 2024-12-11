package ticketaka.mtvs3_final_backend.file.command.application.dto;

import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandResponseDTO;

public class QRResponseDTO {

    public record generateVerificationQRDTO(
            byte[] image,
            String userCode
    ) {
    }

    public record checkVerificationQR(
            int floor,
            int seatNum,
            String seatInfo,
            int seatPrice,
            int concertId,
            String concertName,
            ConcertCommandResponseDTO.timeDTO concertTime
    ) {
    }
}
