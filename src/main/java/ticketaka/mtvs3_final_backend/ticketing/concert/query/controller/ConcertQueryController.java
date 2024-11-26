package ticketaka.mtvs3_final_backend.ticketing.concert.query.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.dto.ConcertQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.service.ConcertQueryService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "04_02ConcertQueryController")
public class ConcertQueryController {

    private final ConcertQueryService concertQueryService;

    /*
        티켓을 보유한 공연장 썸네일 목록 조회
     */
    @GetMapping("/thumbnails")
    public ResponseEntity<?> getConcertThumbnailList() {

        ConcertQueryResponseDTO.getConcertThumbnailList responseDTO = concertQueryService.getConcertThumbnailList(getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
