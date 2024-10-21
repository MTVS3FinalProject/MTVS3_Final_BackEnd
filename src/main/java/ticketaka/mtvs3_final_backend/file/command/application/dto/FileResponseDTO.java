package ticketaka.mtvs3_final_backend.file.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileResponseDTO {

    public record uploadImgForSignUpDTO(
            MultipartFile image,
            String email,
            String secondPwd
    ) {
    }
}
