package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundResponseDTO;

@FeignClient(name = "face-auth-service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface BackgroundFeignClient {


    BackgroundResponseDTO.generateBackgroundDTO generateBackground(BackgroundRequestDTO.generateBackgroundDTO requestDTO);
}
