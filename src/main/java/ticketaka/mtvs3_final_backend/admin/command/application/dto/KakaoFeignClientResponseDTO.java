package ticketaka.mtvs3_final_backend.admin.command.application.dto;

public class KakaoFeignClientResponseDTO {

    public record KakaoTokenDTO(
            String token_type,
            String access_token,
            Long expires_in,
            String refresh_token,
            Long refresh_token_expires_in
    ) {
    }
}
