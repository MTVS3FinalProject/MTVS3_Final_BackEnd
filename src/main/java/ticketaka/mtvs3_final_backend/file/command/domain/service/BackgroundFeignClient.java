package ticketaka.mtvs3_final_backend.file.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "face-auth-service", url = "https://adapted-charmed-panda.ngrok-free.app")
public interface BackgroundFeignClient {


}
