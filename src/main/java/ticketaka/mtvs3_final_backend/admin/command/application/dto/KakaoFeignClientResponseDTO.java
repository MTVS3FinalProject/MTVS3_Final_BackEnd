package ticketaka.mtvs3_final_backend.admin.command.application.dto;

public class KakaoFeignClientResponseDTO {

    public record KakaoTokenDTO(
            String accessToken,
            String tokenType,
            String refreshToken,
            Long expiresIn,
            String scope,
            Long refreshTokenExpiresIn
    ) {
    }
}
