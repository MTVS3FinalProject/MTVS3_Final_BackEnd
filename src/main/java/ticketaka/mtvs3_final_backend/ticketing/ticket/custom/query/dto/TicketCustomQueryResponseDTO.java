package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

import java.util.List;

public class TicketCustomQueryResponseDTO {

    public record getCustomTicketListDTO(
            List<customTicketDTO> customTicketDTOList
    ) {
    }
}
