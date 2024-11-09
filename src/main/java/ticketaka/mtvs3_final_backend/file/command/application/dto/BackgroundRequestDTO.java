package ticketaka.mtvs3_final_backend.file.command.application.dto;

import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;

public class BackgroundRequestDTO {

    public record generateBackgroundDTO(
            Concert concert
    ) {
    }
}
