package ticketaka.mtvs3_final_backend.member.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberCommandResponseDTO;
import ticketaka.mtvs3_final_backend.member.command.application.service.MemberCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "07_MemberCommandController")
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    /*
        타이틀 변경
     */
    @PutMapping("/title/{titleId}")
    public ResponseEntity<?> changeMainTitle(@PathVariable("titleId") Long titleId) {

        MemberCommandResponseDTO.changeMainTitleDTO responseDTO = memberCommandService.changeMainTitle(getCurrentMemberId(), titleId);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        타이틀 해제
     */
    @DeleteMapping("/title")
    public ResponseEntity<?> deleteMainTitle() {

        memberCommandService.deleteMainTitle(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
