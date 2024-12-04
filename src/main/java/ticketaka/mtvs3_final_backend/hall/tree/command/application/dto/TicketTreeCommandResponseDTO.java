package ticketaka.mtvs3_final_backend.hall.tree.command.application.dto;

public class TicketTreeCommandResponseDTO {

    public record registerTicketTree(
            int ticketTreeId,
            String ticketImage
    ) {
    }
}
