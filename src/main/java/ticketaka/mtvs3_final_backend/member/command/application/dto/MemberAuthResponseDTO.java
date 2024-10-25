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

    // 회원 정보
    public record memberInfoDTO(
            String nickname,
            String birth,
            int coin,
            String avatarData
    ) {
    }

    // 로그인 시 반환 정보
    public record loginDTO(
            memberInfoDTO memberInfoDTO,
            authTokenDTO authTokenDTO
    ) {
    }
}
