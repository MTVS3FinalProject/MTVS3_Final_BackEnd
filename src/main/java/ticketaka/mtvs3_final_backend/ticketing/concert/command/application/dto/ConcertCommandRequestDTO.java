package ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto;

public class ConcertCommandRequestDTO {

    public record entranceConcertDTO(
            String concertName
    ) {
    }

    public record acquireStickerFromPuzzleResultDTO(
            int rank
    ) {
    }

    public record enterDeliveryAddressDTO(
            String userName,
            String userPhoneNumber,
            String userAddress1,
            String userAddress2
    ) {
    }
}
