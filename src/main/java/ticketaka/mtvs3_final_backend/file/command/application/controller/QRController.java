package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.file.command.application.service.QRService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qr")
public class QRController {

    private final QRService qrService;

    @GetMapping(value = "/signup", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateSignUpQR() {

        byte[] responseDTO = qrService.generateSignUpQR();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseDTO);
    }
}
