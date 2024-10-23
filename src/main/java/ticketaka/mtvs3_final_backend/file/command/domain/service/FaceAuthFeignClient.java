package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthResponseDTO;

@FeignClient(name = "face-auth-service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface FaceAuthFeignClient {

    @PostMapping("/verificationimage")

    @PostMapping("/verification")
    FaceAuthResponseDTO.identifyFaceDTO identifyFace(@RequestBody FaceAuthRequestDTO.identifyFaceDTO requestDTO);
}