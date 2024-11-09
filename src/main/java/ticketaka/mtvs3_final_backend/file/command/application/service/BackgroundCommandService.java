package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.Background;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.BackgroundCommandRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileCommandRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.service.BackgroundFeignClient;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BackgroundCommandService {

    private final FileCommandService fileCommandService;

    private final BackgroundCommandRepository backgroundCommandRepository;

    private final BackgroundFeignClient backgroundFeignClient;

    /*
        AI 배경 생성
     */
    @Transactional
    public TicketCustomCommandResponseDTO.generateAIBackgroundDTO generateBackground(BackgroundRequestDTO.generateBackgroundDTO requestDTO) {

        byte[] backgroundImageData = backgroundFeignClient.generateBackground();//requestDTO);

        // Background 생성
        Background background = Background.builder()
                .concertId(requestDTO.concert().getId())
                .build();
        background = backgroundCommandRepository.save(background);

        fileCommandService.saveAIBackgroundImage(background.getId(), backgroundImageData);

        return new TicketCustomCommandResponseDTO.generateAIBackgroundDTO(
                background.getId().intValue(),
                backgroundImageData
        );
    }
}
