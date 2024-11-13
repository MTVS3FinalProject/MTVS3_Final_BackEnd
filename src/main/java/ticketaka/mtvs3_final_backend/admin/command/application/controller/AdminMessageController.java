package ticketaka.mtvs3_final_backend.admin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.admin.command.application.service.AdminCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminMessageController {

    private final AdminCommandService adminCommandService;

    /*
        Kakao Message Token 요청
     */
    @GetMapping("/kakao/token")
    public ResponseEntity<?> kakaoToken(@RequestParam(name = "code") String code) {

        adminCommandService.saveKakaoToken(code);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        Kakao Message Callback
     */
    @GetMapping("/message/callback")
    public ResponseEntity<?> messageCallback() {
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
