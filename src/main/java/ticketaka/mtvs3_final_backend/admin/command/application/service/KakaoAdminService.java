package ticketaka.mtvs3_final_backend.admin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.KakaoFeignClientRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.KakaoFeignClientResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.service.KakaoFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KakaoAdminService {

    private final KakaoFeignClient kakaoFeignClient;

    private static final String GRANT_TYPE = "authorization_code";
    @Value(("${KAKAO.CLIENT.ID}"))
    private static String CLIENT_ID;
    @Value(("${KAKAO.REDIRECT.URI}"))
    private static String REDIRECT_URI;

    // Kakao Token 발급
    public KakaoFeignClientResponseDTO.KakaoTokenDTO getKakaoToken(String code) {

        KakaoFeignClientRequestDTO.KakaoTokenDTO requestDTO = new KakaoFeignClientRequestDTO.KakaoTokenDTO(
                GRANT_TYPE,
                CLIENT_ID,
                REDIRECT_URI,
                code
        );

        return kakaoFeignClient.getKakaoToken(requestDTO);
    }
}
