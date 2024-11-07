package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto;

public class TicketCustomCommandResponseDTO {

    public record createAIStickerDTO(
            int stickerId,
            byte[] stickerImage
    ) {
    }
}
