package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.file.command.application.service.FaceAuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/face")
public class FaceAuthController {

    private final FaceAuthService faceAuthService;

    /*
        얼굴 인식
     */
    @PostMapping
    public ResponseEntity<?> identifyFace(@RequestBody String imgUrl) {

        faceAuthService.identifyFace(imgUrl);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
