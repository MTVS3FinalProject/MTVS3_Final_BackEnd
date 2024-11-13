package ticketaka.mtvs3_final_backend.admin.command.domain.dto;

import java.util.List;

public class KakaoFeignClientResponseDTO {

    // KakaoToken 발급
    public record KakaoTokenDTO(
            String token_type,
            String access_token,
            Long expires_in,
            String refresh_token,
            Long refresh_token_expires_in
    ) {
    }

    // Kakao 친구 목록 가져오기
    public record KakaoFriendListDTO(
            List<Friend> elements,
            Integer total_count
    ) {
    }

    public record Friend(
            Long id,
            String uuid
    ) {
    }
}
