package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

import java.util.List;

public class TicketCustomQueryResponseDTO {

    /*
        티켓 커스텀 가능한 공연 리스트 조회
     */
    public record getCustomizableTicketListDTO(
            List<getTicketDTO> ticketDTOList
    ) {
    }

    public record getTicketDTO(
            Long concertId,
            String concertName,
            Long ticketId
    ) {
    }

    /*
        티켓 커스텀 입장
     */
    public record getTicketCustomInfoDTO(
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
