package ticketaka.mtvs3_final_backend.admin.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.KakaoFeignClientResponseDTO;

@FeignClient(name = "kakao-service", url = "https://kauth.kakao.com")
public interface KakaoFeignClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoFeignClientResponseDTO.KakaoTokenDTO getKakaoToken(@RequestParam("grant_type") String grantType,
                                                            @RequestParam("client_id") String clientId,
                                                            @RequestParam("redirect_uri") String redirectUri,
                                                            @RequestParam("code") String code);

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoFeignClientResponseDTO.KakaoTokenDTO reissueKakaoToken(@RequestParam("grant_type") String grantType,
                                                                @RequestParam("client_id") String clientId,
                                                                @RequestParam("refresh_token") String refreshToken);

    @GetMapping(value = "/v1/api/talk/friends")
    KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriends(@RequestHeader("Authorization") String accessToken);
}
