package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberResponseDTO {

    // 배송지 입력
    public record enterDeliveryAddressDTO(
            String seatInfo,
            int seatPrice,
            int userCoin,
            int needCoin
    ) {
    }
}