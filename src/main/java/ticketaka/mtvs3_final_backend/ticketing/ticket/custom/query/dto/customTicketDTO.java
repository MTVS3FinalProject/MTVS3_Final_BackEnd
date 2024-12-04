package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto;

public record customTicketDTO(
        int ticketId,
        String customTicketImage
) {
    public customTicketDTO(Long ticketId, String fileUrl) {
        this(ticketId.intValue(), fileUrl);
    }
}