package ticketaka.mtvs3_final_backend.concert.command.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertRequestDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertResponseDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.service.ConcertService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concerts")
@Tag(name = "04_ConcertController")
public class ConcertController {

    private final ConcertService concertService;

    /*
        공연장 정보 조회
     */
    @GetMapping
    public ResponseEntity<?> getConcertList() {

        ConcertResponseDTO.getConcertListDTO responseDTO = concertService.getConcertList();

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        공연장 입장
     */
    @GetMapping("/{concertId}")
    public ResponseEntity<?> entranceConcert(@PathVariable("concertId") int concertId) {
        
        ConcertResponseDTO.entranceConcertDTO responseDTO = concertService.entranceConcert((long) concertId, getCurrentMemberId());
        
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    /*
        예매자 정보 입력
     */
    @PostMapping("/member/delivery-address")
    public ResponseEntity<?> concert(@RequestBody ConcertRequestDTO.enterDeliveryAddressDTO requestDTO) {

        ConcertResponseDTO.enterDeliveryAddressDTO responseDTO = concertService.enterDeliveryAddress(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
