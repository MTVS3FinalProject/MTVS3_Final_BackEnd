package ticketaka.mtvs3_final_backend.concert.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertRequestDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertResponseDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.service.ConcertService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    /*
        공연장 입장
     */
    @PostMapping
    public ResponseEntity<?> entranceConcertDTO(@RequestBody ConcertRequestDTO.entranceConcertDTO requestDTO) {
        
        ConcertResponseDTO.entranceConcertDTO responseDTO = null;
        
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
