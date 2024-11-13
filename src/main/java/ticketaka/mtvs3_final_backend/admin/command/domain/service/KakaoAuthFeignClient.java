package ticketaka.mtvs3_final_backend.admin.command.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ticketaka.mtvs3_final_backend.admin.command.domain.dto.KakaoFeignClientResponseDTO;

@FeignClient(name = "kakao-auth-service", url = "https://kauth.kakao.com")
public interface KakaoAuthFeignClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoFeignClientResponseDTO.KakaoTokenDTO getKakaoToken(@RequestParam("grant_type") String grantType,
                                                            @RequestParam("client_id") String clientId,
                                                            @RequestParam("redirect_uri") String redirectUri,
                                                            @RequestParam("code") String code);

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoFeignClientResponseDTO.KakaoTokenDTO reissueKakaoToken(@RequestParam("grant_type") String grantType,
                                                                @RequestParam("client_id") String clientId,
                                                                @RequestParam("refresh_token") String refreshToken);
}
