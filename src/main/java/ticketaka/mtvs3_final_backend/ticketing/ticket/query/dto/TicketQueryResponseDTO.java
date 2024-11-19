package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

import java.util.List;

public class TicketQueryResponseDTO {

    /*
        커스텀 티켓 목록 조회
     */
    public record getCustomizableTicketListDTO(
            List<getTicketDTO> ticketDTOList
    ) {
    }

    /*
        티켓 커스텀 입장
     */
    public record getTicketCustomObjectDTO(
            int dailyBackgroundRefreshCount,
            List<stickerDTO> stickerDTOList,
            List<ticketDTO> ticketDTOList
    ) {
    }

    public record ticketDTO(
            int ticketId,
            String concertName,
            int year,
            int month,
            int day,
            String time,
            String seatInfo
    ) {
    }
}
