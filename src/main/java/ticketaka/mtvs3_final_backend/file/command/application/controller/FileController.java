package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FileResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    /*
        파일 업로드 - 회원 가입 용
     */
    @PostMapping("/signup")
    public ResponseEntity<?> uploadImgForSignUp(@ModelAttribute FileResponseDTO.uploadImgForSignUpDTO requestDTO) {

        // 이메일 정보와 이미지 처리
        fileService.uploadImgForSignUp(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
