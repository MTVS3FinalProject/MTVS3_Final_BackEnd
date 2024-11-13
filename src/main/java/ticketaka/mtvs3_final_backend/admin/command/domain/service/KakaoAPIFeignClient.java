package ticketaka.mtvs3_final_backend.admin.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.KakaoFeignClientResponseDTO;

@FeignClient(name = "kakao-api-service", url = "https://kapi.kakao.com")
public interface KakaoAPIFeignClient {

    @GetMapping(value = "/v1/api/talk/friends")
    KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriends(@RequestHeader("Authorization") String accessToken);
}
