package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberAuthResponseDTO {

    // 토큰 발급
    public record authTokenDTO(
            String grantType,
            String accessToken,
            Long accessTokenValidTime,
            String refreshToken,
            Long refreshTokenValidTime
    ) {
    }
}
