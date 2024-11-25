package ticketaka.mtvs3_final_backend.ticketing.concert.query.dto;

import java.util.List;

public class ConcertQueryResponseDTO {

    public record getConcertThumbnailList(
            List<String> concertThumbnailList
    ) {
    }
}
