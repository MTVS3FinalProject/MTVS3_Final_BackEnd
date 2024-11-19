package ticketaka.mtvs3_final_backend.mail.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.mail.command.application.dto.MailCommandResponseDTO;
import ticketaka.mtvs3_final_backend.mail.command.application.service.MailCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/mails")
public class MailCommandController {

    private final MailCommandService mailCommandService;

    /*
        특정 우편 조회
     */
    @GetMapping("/{mailId}")
    public ResponseEntity<?> readMail(@PathVariable("mailId") Long mailId) {

        MailCommandResponseDTO.readMailDTO responseDTO = mailCommandService.readMail(getCurrentMemberId(), mailId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
