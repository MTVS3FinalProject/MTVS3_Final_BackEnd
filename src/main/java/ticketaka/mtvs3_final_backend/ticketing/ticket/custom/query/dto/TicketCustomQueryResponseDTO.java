package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

import java.util.List;

public class TicketCustomQueryResponseDTO {

    public record getCustomTicketDTO(
            List<customTicketDTO> customTicketDTOList
    ) {
    }

    public record customTicketDTO(
            int ticketId,
            String customTicketImage
    ) {
    }
}
