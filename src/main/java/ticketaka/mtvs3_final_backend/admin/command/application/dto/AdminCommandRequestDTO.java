package ticketaka.mtvs3_final_backend.admin.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

public class AdminCommandRequestDTO {

    // Sticker 업로드
    public record uploadStickerDTO(
            String stickerName,
            String stickerScript,
            String stickerType,
            String stickerRarity,
            MultipartFile stickerImage
    ) {
    }
}
