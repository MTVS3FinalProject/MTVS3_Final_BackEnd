package ticketaka.mtvs3_final_backend.file.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileRequestDTO {

    public record uploadUserImgDTO(
            MultipartFile image
    ) {
    }
}
