package ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto;

import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.TicketStatus;

import java.time.LocalDateTime;

public record getTicketDetailDTO(
        int ticketId,
        String concertName,
        int year,
        int month,
        int day,
        String time,
        String seatInfo,
        String ticketImage,
        String backgroundImage,
        String qrImage,
        Boolean isUsed
) {
    public getTicketDetailDTO(Long ticketId, String concertName, LocalDateTime concertDate, String seatInfo, String ticketImage, String backgroundImage, String qrImage, Boolean isUsed) {
        this(ticketId.intValue(),
                concertName,
                concertDate.getYear(),
                concertDate.getMonthValue(),
                concertDate.getDayOfMonth(),
                concertDate.toLocalTime().toString(),
                seatInfo,
                ticketImage,
                backgroundImage,
                qrImage,
                isUsed
        );
    }
}