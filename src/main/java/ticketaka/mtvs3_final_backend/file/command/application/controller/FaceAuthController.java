package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.file.command.application.service.FaceAuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/face")
public class FaceAuthController {

    private final FaceAuthService faceAuthService;


}
