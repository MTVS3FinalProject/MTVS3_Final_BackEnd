package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    // 파일 업로드
    String uploadImage(MultipartFile file, String fileName, String contentType);
}
