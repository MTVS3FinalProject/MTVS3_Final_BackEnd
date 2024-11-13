package ticketaka.mtvs3_final_backend.admin.command.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.KakaoFeignClientResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.model.KakaoToken;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.KakaoTokenRepository;
import ticketaka.mtvs3_final_backend.admin.command.domain.service.KakaoFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KakaoAdminService {

    private final KakaoFeignClient kakaoFeignClient;
    private final KakaoTokenRepository kakaoTokenRepository;

    private static final String ACCESS_TOKEN_GRANT_TYPE = "authorization_code";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    @Value(("${KAKAO.CLIENT.ID}"))
    private String CLIENT_ID;
    @Value(("${KAKAO.REDIRECT.URI}"))
    private String REDIRECT_URI;

    private static final String AUTHORIZATION_GRANT_TYPE = "Bearer ";

    // Kakao Token 발급
    public KakaoFeignClientResponseDTO.KakaoTokenDTO getKakaoToken(String code) {

        return kakaoFeignClient.getKakaoToken(
                ACCESS_TOKEN_GRANT_TYPE,
                CLIENT_ID,
                REDIRECT_URI,
                code
        );
    }

    // Kakao 친구 목록 조회
    public KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriendList(KakaoToken kakaoToken) {

        try {
            String accessToken = AUTHORIZATION_GRANT_TYPE + kakaoToken.getAccessToken();
            return kakaoFeignClient.getKakaoFriends(accessToken);
        } catch (FeignException e) {
            if (e.status() == 401) {
                log.warn("Kakao token is expired");

                KakaoFeignClientResponseDTO.KakaoTokenDTO kakaoTokenDTO = kakaoFeignClient.reissueKakaoToken(
                        REFRESH_TOKEN_GRANT_TYPE,
                        CLIENT_ID,
                        kakaoToken.getRefreshToken()
                );
                KakaoToken newKakaoToken = saveKakaoToken(kakaoTokenDTO);

                String accessToken = AUTHORIZATION_GRANT_TYPE + newKakaoToken.getAccessToken();
                return kakaoFeignClient.getKakaoFriends(accessToken);
            } else {
                throw new Exception401("Kakao token is expired");
            }
        }
    }

    public KakaoToken saveKakaoToken(KakaoFeignClientResponseDTO.KakaoTokenDTO responseDTO) {
        // Kakao Token 저장
        KakaoToken kakaoToken = KakaoToken.builder()
                .tokenType(responseDTO.token_type())
                .accessToken(responseDTO.access_token())
                .expiresIn(responseDTO.expires_in())
                .refreshToken(responseDTO.refresh_token())
                .refreshTokenExpiresIn(responseDTO.refresh_token_expires_in())
                .build();
        return kakaoTokenRepository.save(kakaoToken);
    }
}
