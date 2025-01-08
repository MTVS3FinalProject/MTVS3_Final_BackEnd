package ticketaka.mtvs3_final_backend.member.query.dto;

import java.time.LocalDateTime;

public record getMemberTicketDTO(
        int ticketId,
        String concertName,
        String seatInfo,
        String ticketImage,
        int year,
        int month,
        int day,
        String time
) {
    public getMemberTicketDTO(Long ticketId, String concertName, String seatInfo, String ticketImage, LocalDateTime concertTime) {
        this(ticketId.intValue(), concertName, seatInfo, ticketImage,
                concertTime.getYear(), concertTime.getMonthValue(), concertTime.getDayOfMonth(), concertTime.toLocalTime().toString());
    }
}
