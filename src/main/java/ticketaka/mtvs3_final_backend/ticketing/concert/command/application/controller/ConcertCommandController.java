package ticketaka.mtvs3_final_backend.ticketing.concert.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.service.ConcertCommandService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "04_ConcertController")
public class ConcertCommandController {

    private final ConcertCommandService concertCommandService;

    /*
        공연장 정보 조회
     */
    @GetMapping
    public ResponseEntity<?> getConcertList() {

        ConcertCommandResponseDTO.getConcertListDTO responseDTO = concertCommandService.getConcertList();

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        공연장 입장
     */
    @GetMapping("/{concertId}")
    public ResponseEntity<?> entranceConcert(@PathVariable("concertId") Long concertId) {
        
        ConcertCommandResponseDTO.entranceConcertDTO responseDTO = concertCommandService.entranceConcert(concertId, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        Puzzle 결과 Sticker 획득
     */
    @PostMapping("/{concertId}/puzzle/result")
    public ResponseEntity<?> acquireStickerFromPuzzleResult(@PathVariable("concertId") Long concertId,
                                                            @RequestBody ConcertCommandRequestDTO.acquireStickerFromPuzzleResultDTO requestDTO) {

        ConcertCommandResponseDTO.acquireStickerFromPuzzleResultDTO responseDTO = concertCommandService.acquireStickerFromPuzzleResult(getCurrentMemberId(), concertId, requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        예매자 정보 입력
     */
    @PostMapping("/{concertId}/seats/{seatId}/reservation")
    public ResponseEntity<?> concert(@PathVariable("concertId") Long concertId,
                                     @PathVariable("seatId") Long seatId,
                                     @RequestBody ConcertCommandRequestDTO.enterDeliveryAddressDTO requestDTO) {

        ConcertCommandResponseDTO.enterDeliveryAddressDTO responseDTO = concertCommandService.enterDeliveryAddress(getCurrentMemberId(), concertId, seatId, requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
