package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    // 파일 업로드 - S3
    String uploadImageByS3(MultipartFile file, String fileName, String contentType);
    // 파일 업로드 - FireBase
    String uploadImageByFireBase(MultipartFile file, String fileName, String contentType);
}
