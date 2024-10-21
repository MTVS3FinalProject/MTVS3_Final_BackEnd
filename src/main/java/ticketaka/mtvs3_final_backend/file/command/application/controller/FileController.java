package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    /*
        파일 업로드 - 회원 가입 용
        TODO: 비밀번호 추가
     */
    @PostMapping("/signup")
    public ResponseEntity<?> uploadImgForSignUp(@RequestParam("image") MultipartFile image,
                                                @RequestParam("email") String email) {

        // 이메일 정보와 이미지 처리
        fileService.uploadImgForSignUp(image, email);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
