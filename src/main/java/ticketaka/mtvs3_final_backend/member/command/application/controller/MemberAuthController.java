package ticketaka.mtvs3_final_backend.member.command.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.member.command.application.service.MemberAuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    /*
        기본 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberAuthRequestDTO.signUpDTO requestDTO) {

        memberAuthService.signUp(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
