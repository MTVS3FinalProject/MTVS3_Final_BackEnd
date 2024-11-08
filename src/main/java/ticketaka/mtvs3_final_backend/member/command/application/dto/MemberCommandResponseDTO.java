package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberCommandResponseDTO {

    public record acquireStickerFromPuzzleResultDTO(
            int stickerId,
            String stickerName,
            String stickerScript,
            String stickerRarity
    ) {
    }
}
