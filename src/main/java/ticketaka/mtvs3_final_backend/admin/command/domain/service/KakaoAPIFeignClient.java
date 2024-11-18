package ticketaka.mtvs3_final_backend.admin.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend.admin.command.domain.dto.KakaoFeignClientResponseDTO;

@FeignClient(name = "kakao-api-service", url = "https://kapi.kakao.com")
public interface KakaoAPIFeignClient {

    @GetMapping(value = "/v1/api/talk/friends")
    KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriends(@RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "v1/api/talk/friends/message/send")
    void sendKakaoMessage(@RequestHeader("Authorization") String accessToken,
                          @RequestParam("receiver_uuids") String receiverUuids,
                          @RequestParam("template_id") Long templateId);
}
