package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundResponseDTO;

@FeignClient(name = "background-service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface BackgroundFeignClient {

    @PostMapping("/img_random")
    BackgroundResponseDTO.generateBackgroundDTO generateBackground();//@RequestBody BackgroundRequestDTO.generateBackgroundDTO requestDTO);
}
