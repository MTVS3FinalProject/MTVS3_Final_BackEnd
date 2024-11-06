package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

import java.util.List;

public class TicketCustomQueryResponseDTO {

    /*
        티켓 커스텀 가능한 공연 리스트 조회
     */
    public record getTicketListDTO(
            List<getTicketDTO> ticketDTOList
    ) {
    }

    public record getTicketDTO(
            Long concertId,
            Long ticketId
    ) {
    }
}
