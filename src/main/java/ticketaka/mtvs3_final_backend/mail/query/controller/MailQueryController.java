package ticketaka.mtvs3_final_backend.mail.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.mail.query.dto.MailQueryResponseDTO;
import ticketaka.mtvs3_final_backend.mail.query.service.MailQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/mails")
public class MailQueryController {

    private final MailQueryService mailQueryService;

    /*
        우편 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> getMailList() {

        MailQueryResponseDTO.getMailListDTO responseDTO = mailQueryService.getMailList(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
