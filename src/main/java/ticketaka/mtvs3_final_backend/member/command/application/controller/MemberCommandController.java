package ticketaka.mtvs3_final_backend.member.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend.member.command.application.service.MemberCommandService;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concert")
@Tag(name = "07_MemberCommandController")
public class MemberCommandController {

    private final MemberCommandService memberCommandService;
}
