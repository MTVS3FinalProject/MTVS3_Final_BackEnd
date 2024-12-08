package ticketaka.mtvs3_final_backend.staff.concert.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.staff.concert.command.application.dto.StaffConcertCommandRequestDTO;
import ticketaka.mtvs3_final_backend.staff.concert.command.application.service.StaffConcertCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/staff/concerts")
@Tag(name = "StaffConcertCommandController")
public class StaffConcertCommandController {

    private final StaffConcertCommandService staffConcertCommandService;

    /*
        공연 생성
     */
    @PostMapping
    public ResponseEntity<?> createConcert(@RequestBody StaffConcertCommandRequestDTO.createConcertDTO requestDTO) {

        staffConcertCommandService.createConcert(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
