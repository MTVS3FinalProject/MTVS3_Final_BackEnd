package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto;

public class TicketResponseDTO {

    public record createTicketDTO(
            Long ticketId
    ) {
    }
}
