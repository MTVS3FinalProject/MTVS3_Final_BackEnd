package ticketaka.mtvs3_final_backend.hall.tree.query.dto;

import java.util.List;

public class TicketTreeQueryRequestDTO {

    public record getTicketTreeDTO(
            List<ticketTreeDTO> ticketTreeDTOList
    ) {
    }

    public record ticketTreeDTO(
            int ticketTreeId,
            String ticketImage
    ) {
    }
}
