package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FileRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    /*
        파일 업로드 테스트
     */
    @PostMapping
    public ResponseEntity<?> uploadImg(@RequestBody FileRequestDTO.uploadUserImgDTO requestDTO) throws IOException {

        MultipartFile image = requestDTO.image();
        fileService.uploadFirebaseBucket(image, image.getOriginalFilename());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
