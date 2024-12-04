package ticketaka.mtvs3_final_backend.hall.tree.command.application.dto;

public class TicketTreeCommandResponseDTO {

    public record registerTicketTreeDTO(
            int ticketTreeId,
            String ticketImage
    ) {
    }
}
