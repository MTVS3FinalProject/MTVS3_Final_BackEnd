package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto;

import java.util.List;

public class TicketCommandRequestDTO {

    public record saveCustomTicketDTO(
            byte[] customTicketImage,
            List<Integer> stickerIdList,
            Integer backgroundId
    ) {
    }
}
