package ticketaka.mtvs3_final_backend.file.command.domain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "face_auth_service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface FaceAuthFeignClient {

    @PostMapping("/verification")
    void identifyFaceAuth(@RequestBody String check);
}