package ticketaka.mtvs3_final_backend.member.query.dto;

public record getMemberTicketDTO(
        int ticketId,
        String concertName,
        String seatInfo,
        String ticketImage
) {
    public getMemberTicketDTO(Long ticketId, String concertName, String seatInfo, String ticketImage) {
        this(ticketId.intValue(), concertName, seatInfo, ticketImage);
    }
}
