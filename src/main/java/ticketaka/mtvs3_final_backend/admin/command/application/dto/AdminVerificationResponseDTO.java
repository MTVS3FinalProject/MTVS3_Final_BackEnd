package ticketaka.mtvs3_final_backend.admin.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class AdminVerificationResponseDTO {

    public record verifyTicketDTO(
            Long ticketId,
            String concertName,
            LocalDateTime concertDate,
            String seatInfo
    ) {
    }

    // 티켓 사용 파일 업로드
    public record verifyTicketOwnerDTO(
            MultipartFile image,
            Long ticketId
    ) {
    }
}
