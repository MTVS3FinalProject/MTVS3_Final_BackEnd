package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class TicketCommandRequestDTO {

    public record saveCustomTicketDTO(
            MultipartFile customTicketImage,
            List<Integer> stickerIdList,
            Integer backgroundId
    ) {
    }
}
