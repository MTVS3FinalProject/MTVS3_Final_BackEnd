package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.FaceAuthService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/face")
public class FaceAuthController {

    private final FaceAuthService faceAuthService;

    /*
        얼굴 인식
     */
    @PostMapping("/verification")
    public ResponseEntity<?> verificationMember(@RequestBody FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        faceAuthService.verificationMember(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
