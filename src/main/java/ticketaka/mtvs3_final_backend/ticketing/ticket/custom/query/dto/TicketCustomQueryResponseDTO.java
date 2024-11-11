package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

import java.util.List;

public class TicketCustomQueryResponseDTO {

    /*
        티켓 커스텀 입장
     */
    public record getTicketCustomObjectDTO(
            int dailyBackgroundRefreshCount,
            List<stickerDTO> stickerDTOList
    ) {
    }

    // Sticker
    public record stickerDTO(
            int stickerId,
            byte[] stickerImage
    ) {
    }
}
