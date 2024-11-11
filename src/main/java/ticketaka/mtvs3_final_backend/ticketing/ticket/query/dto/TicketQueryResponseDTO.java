package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;

import java.util.List;

public class TicketQueryResponseDTO {

    /*
        커스텀 티켓 목록 조회
     */
    public record getCustomizableTicketListDTO(
            List<TicketCustomQueryResponseDTO.getTicketDTO> ticketDTOList
    ) {
    }

    public record getTicketDTO(
            TicketCustomQueryResponseDTO.ticketConcertDTO concertInfo,
            String seatInfo,
            int ticketId,
            byte[] ticketImage
    ) {
    }
}
