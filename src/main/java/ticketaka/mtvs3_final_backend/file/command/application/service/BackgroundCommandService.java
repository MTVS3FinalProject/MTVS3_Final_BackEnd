package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.service.BackgroundFeignClient;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BackgroundCommandService {

    private final BackgroundFeignClient backgroundFeignClient;

    /*
        AI 배경 생성
     */
    public TicketCustomCommandResponseDTO.generateAIBackgroundDTO generateBackground(BackgroundRequestDTO.generateBackgroundDTO requestDTO) {

        BackgroundResponseDTO.generateBackgroundDTO responseDTO = backgroundFeignClient.generateBackground(requestDTO);

        // Img 저장 및 byte[] 반환

        return null;
    }
}
