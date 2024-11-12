package ticketaka.mtvs3_final_backend.file.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.QRCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qr")
@Tag(name = "02_QRController")
public class QRController {

    private final QRCommandService qrCommandService;

    /*
        회원 가입 용 QR 생성
     */
    @PostMapping(value = "/signup", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateSignUpQR(@RequestBody @Valid QRRequestDTO.generateSignUpQRDTO requestDTO) {

        System.out.println("requestDTO = " + requestDTO);

        byte[] responseDTO = qrCommandService.generateSignUpQR(requestDTO);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }

    /*
        회원 가입 용 사진 업로드 성공 확인
     */
    @PostMapping("/signup/success")
    public ResponseEntity<?> checkSignUpQR(@RequestBody QRRequestDTO.checkSignUpQRDTO requestDTO) {

        System.out.println("requestDTO = " + requestDTO);

        qrCommandService.checkSignUpQR(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        회원 인증 용 QR 생성
     */
    @GetMapping( "/verification")
    public ResponseEntity<?> generateVerificationQR() {

        QRResponseDTO.generateVerificationQRDTO responseDTO = qrCommandService.generateVerificationQR(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        회원 인증 용 사진 업로드 성공 확인
     */
    @PostMapping(value = "/verification/success")
    public ResponseEntity<?> checkVerificationQR(@RequestBody QRRequestDTO.checkVerificationQRDTO requestDTO) {

        QRResponseDTO.checkVerificationQR responseDTO = qrCommandService.checkVerificationQR(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
