package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;
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
    @GetMapping(value = "/signup", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateSignUpQR(@RequestBody QRRequestDTO.generateQRDTO requestDTO) {

        byte[] responseDTO = qrService.generateSignUpQR(requestDTO);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }

    /*
        회원 가입 용 사진 업로드 성공 확인
     */
    @GetMapping("/signup/success")
    public ResponseEntity<?> checkSignUpQR(@RequestBody QRRequestDTO.generateQRDTO requestDTO) {

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        회원 인증 용 QR 생성
     */
    @GetMapping(value = "/verification", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateVerificationQR() {

        byte[] responseDTO = qrService.generateVerificationQR(getCurrentMemberId());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }

    /*
        회원 인증 용 사진 업로드 성공 확인
     */
    @GetMapping(value = "/verification/success", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> checkVerificationQR() {

        byte[] responseDTO = qrService.generateVerificationQR(getCurrentMemberId());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }
}
