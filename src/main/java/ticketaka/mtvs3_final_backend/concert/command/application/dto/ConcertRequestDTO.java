package ticketaka.mtvs3_final_backend.concert.command.application.dto;

public class ConcertRequestDTO {

    public record entranceConcertDTO(
            String concertName
    ) {
    }

    public record enterDeliveryAddressDTO(
            String userName,
            int userPhoneNumber,
            String userAddress1,
            String userAddress2
    ) {
    }
}
