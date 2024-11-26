package ticketaka.mtvs3_final_backend.admin.command.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.admin.command.domain.dto.KakaoFeignClientResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.model.KakaoToken;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.KakaoTokenRepository;
import ticketaka.mtvs3_final_backend.admin.command.domain.service.KakaoAPIFeignClient;
import ticketaka.mtvs3_final_backend.admin.command.domain.service.KakaoAuthFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KakaoAdminService {

    private final KakaoAuthFeignClient kakaoAuthFeignClient;
    private final KakaoTokenRepository kakaoTokenRepository;

    private static final String ACCESS_TOKEN_GRANT_TYPE = "authorization_code";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    private final KakaoAPIFeignClient kakaoAPIFeignClient;
    @Value("${KAKAO.CLIENT.ID}")
    private String CLIENT_ID;
    @Value("${KAKAO.REDIRECT.URI}")
    private String REDIRECT_URI;

    private static final String AUTHORIZATION_GRANT_TYPE = "Bearer ";
    @Value("${KAKAO.MESSAGE.TEMPLATE.ID}")
    private Long KAKAO_MESSAGE_TEMPLATE;

    // Kakao Token 발급
    public KakaoFeignClientResponseDTO.KakaoTokenDTO getKakaoToken(String code) {

        return kakaoAuthFeignClient.getKakaoToken(
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
            log.info("getKakaoFriendList_accessToken: {}", accessToken);

            return kakaoAPIFeignClient.getKakaoFriends(accessToken);
        } catch (FeignException e) {
            log.error("getKakaoFriendList_Kakao API error: {}", e.content());
            if (e.status() == 401) {
                log.warn("getKakaoFriendList_Kakao token is expired");

                KakaoFeignClientResponseDTO.KakaoTokenDTO kakaoTokenDTO = kakaoAuthFeignClient.reissueKakaoToken(
                        REFRESH_TOKEN_GRANT_TYPE,
                        CLIENT_ID,
                        kakaoToken.getRefreshToken()
                );
                KakaoToken newKakaoToken = saveKakaoToken(kakaoTokenDTO);

                String accessToken = AUTHORIZATION_GRANT_TYPE + newKakaoToken.getAccessToken();
                return kakaoAPIFeignClient.getKakaoFriends(accessToken);
            } else {
                throw new Exception401("Kakao token is expired");
            }
        }
    }

    // Kakao 친구 메세지 전송
    public void sendKakaoMessage(KakaoToken kakaoToken, String userName) {
        try {
            String accessToken = AUTHORIZATION_GRANT_TYPE + kakaoToken.getAccessToken();
            log.info("sendKakaoMessage_accessToken: {}", accessToken);

            // 친구 목록 조회
            KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO = kakaoAPIFeignClient.getKakaoFriends(accessToken);

            checkKakaoFriendListDTO(kakaoFriendListDTO);

            // 친구 목록에서 UUID 추출
            kakaoAPIFeignClient.sendKakaoMessage(kakaoToken.getAccessToken(), formatSendKakaoMessageToAllDTO(kakaoFriendListDTO), KAKAO_MESSAGE_TEMPLATE);

        } catch (FeignException e) {

            if (e.status() == 401) {
                KakaoFeignClientResponseDTO.KakaoTokenDTO kakaoTokenDTO = kakaoAuthFeignClient.reissueKakaoToken(
                        REFRESH_TOKEN_GRANT_TYPE,
                        CLIENT_ID,
                        kakaoToken.getRefreshToken()
                );
                KakaoToken newKakaoToken = saveKakaoToken(kakaoTokenDTO);

                String accessToken = AUTHORIZATION_GRANT_TYPE + newKakaoToken.getAccessToken();
                kakaoAPIFeignClient.sendKakaoMessage(accessToken, formatSendKakaoMessageToAllDTO(kakaoAPIFeignClient.getKakaoFriends(accessToken)), KAKAO_MESSAGE_TEMPLATE);
            } else {
                throw new Exception401("sendKakaoMessage_Kakao token is expired");
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

    private String formatSendKakaoMessageToAllDTO(KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO) {

        StringBuilder uuidList = new StringBuilder();
        uuidList.append("[");

        for (KakaoFeignClientResponseDTO.Friend friend : kakaoFriendListDTO.elements()) {
            if (uuidList.length() > 1) {
                uuidList.append(",");
            }
            uuidList.append("\"").append(friend.uuid()).append("\"");
        }
        uuidList.append("]");

        return uuidList.toString();
    }

    private String formatKakaoFriendUUID(KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO, String userName) {

        StringBuilder userUUID = new StringBuilder();
        userUUID.append("[\"");

        for (KakaoFeignClientResponseDTO.Friend friend : kakaoFriendListDTO.elements()) {
            if (friend.profile_nickname().equals(userName)) {
                userUUID.append(friend.uuid());
            }
        }

        userUUID.append("\"]");

        return userUUID.toString();
    }

    private void checkKakaoFriendListDTO(KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO) {
        if (kakaoFriendListDTO == null || kakaoFriendListDTO.elements().isEmpty()) {
            throw new Exception400("Kakao 친구 목록이 비어있습니다.");
        }
    }
}
