package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.QRService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qr")
public class QRController {

    private final QRService qrService;

    /*
        회원 가입 용 QR 생성
     */
    @PostMapping(value = "/signup", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateSignUpQR(@RequestBody QRRequestDTO.generateSignUpQRDTO requestDTO) {

        System.out.println("requestDTO = " + requestDTO);

        byte[] responseDTO = qrService.generateSignUpQR(requestDTO);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }

    /*
        회원 가입 용 사진 업로드 성공 확인
     */
    @PostMapping("/signup/success")
    public ResponseEntity<?> checkSignUpQR(@RequestBody QRRequestDTO.generateSignUpQRDTO requestDTO) {

        System.out.println("requestDTO = " + requestDTO);

        qrService.checkSignUpQR(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        회원 인증 용 QR 생성
     */
    @GetMapping( "/verification")
    public ResponseEntity<?> generateVerificationQR() {

        QRResponseDTO.generateVerificationQRDTO responseDTO = qrService.generateVerificationQR(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        회원 인증 용 사진 업로드 성공 확인
     */
    @PostMapping(value = "/verification/success")
    public ResponseEntity<?> checkVerificationQR(@RequestBody QRRequestDTO.checkVerificationQRDTO requestDTO) {

        qrService.checkVerificationQR(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
