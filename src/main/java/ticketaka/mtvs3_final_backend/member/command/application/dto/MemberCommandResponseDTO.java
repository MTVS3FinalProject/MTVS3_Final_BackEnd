package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberCommandResponseDTO {

    public record changeMainTitleDTO(
            String titleName,
            String titleRarity
    ) {
    }
}
