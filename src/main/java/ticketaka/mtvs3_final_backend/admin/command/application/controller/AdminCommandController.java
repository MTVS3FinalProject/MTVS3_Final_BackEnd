package ticketaka.mtvs3_final_backend.admin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminCommandRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.application.service.AdminCommandService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminCommandController {

    private final AdminCommandService adminCommandService;

    /*
        Title 추가
     */
    @PostMapping("/concerts/{concertId}/title")
    public ResponseEntity<?> uploadTitle(@PathVariable("concertId") Long concertId, @RequestBody AdminCommandRequestDTO.uploadTitleDTO requestDTO) {

        adminCommandService.uploadTitle(concertId, requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /*
        Sticker 추가
     */
    @PostMapping("/concerts/{concertId}/sticker")
    public ResponseEntity<?> uploadSticker(@PathVariable("concertId") Long concertId, @ModelAttribute AdminCommandRequestDTO.uploadStickerDTO requestDTO) {

        adminCommandService.uploadSticker(concertId, requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
