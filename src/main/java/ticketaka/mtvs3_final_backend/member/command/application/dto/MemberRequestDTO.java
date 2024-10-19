package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberRequestDTO {

    // 배송지 입력
    public record enterDeliveryAddress(
            String userName,
            int phoneNumber,
            String userAddress
    ) {
    }
}
