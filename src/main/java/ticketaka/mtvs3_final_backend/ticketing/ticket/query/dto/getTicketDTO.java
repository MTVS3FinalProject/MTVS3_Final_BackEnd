package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

import java.time.LocalDateTime;

public record getTicketDTO(
        int ticketId,
        String concertName,
        int year,
        int month,
        int day,
        String time,
        String seatInfo,
        String ticketImage,
        String qrImage
) {
    public getTicketDTO(Long ticketId, String concertName, LocalDateTime concertDate, String seatInfo, String fileImage, String qrImage) {
        this(ticketId.intValue(),
                concertName,
                concertDate.getYear(),
                concertDate.getMonthValue(),
                concertDate.getDayOfMonth(),
                concertDate.toLocalTime().toString(),
                seatInfo,
                fileImage,
                qrImage
        );
    }
}
