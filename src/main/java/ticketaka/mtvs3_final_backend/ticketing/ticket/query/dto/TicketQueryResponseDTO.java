package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;

import java.util.List;

public class TicketQueryResponseDTO {

    /*
        커스텀 티켓 목록 조회
     */
    public record getCustomizableTicketListDTO(
            List<getTicketDTO> ticketDTOList
    ) {
    }

    public record getTicketDTO(
            ticketConcertDTO concertInfo,
            String seatInfo,
            int ticketId,
            byte[] ticketImage
    ) {
    }

    // 공연 날짜
    public record ticketConcertDTO(
            String concertName,
            int year,
            int month,
            int day,
            String time
    ) {
    }
}
