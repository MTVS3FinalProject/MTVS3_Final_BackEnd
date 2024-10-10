package ticketaka.mtvs3_final_backend.file.command.domain;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "face_auth_service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface FaceAuthFeignClient {

}