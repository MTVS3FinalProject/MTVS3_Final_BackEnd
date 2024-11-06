package ticketaka.mtvs3_final_backend.member.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.member.query.service.MemberQueryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "06_MemberQueryController")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;
}
