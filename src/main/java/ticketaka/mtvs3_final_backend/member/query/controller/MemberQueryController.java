package ticketaka.mtvs3_final_backend.member.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.service.MemberQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "06_MemberQueryController")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    /*
        최근 배송 정보 조회
     */
    @GetMapping("/address")
    public ResponseEntity<?> getRecentMemberAddress() {

        MemberQueryResponseDTO.getRecentMemberAddressDTO responseDTO = memberQueryService.getRecentMemberAddress(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        인벤토리 조회
     */
    @GetMapping("/inventory")
    public ResponseEntity<?> getMemberInventory() {

        MemberQueryResponseDTO.getMemberInventoryDTO responseDTO = memberQueryService.getMemberInventory(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        티켓 사용 권한 확인
     */
    @GetMapping("/tickets/{ticketId}/verification")
    public ResponseEntity<?> checkTicketVerification(@PathVariable("ticketId") Long ticketId) {

        memberQueryService.checkTicketVerification(getCurrentMemberId(), ticketId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
