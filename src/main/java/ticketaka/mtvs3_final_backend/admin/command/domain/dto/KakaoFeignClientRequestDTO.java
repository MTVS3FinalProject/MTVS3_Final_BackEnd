package ticketaka.mtvs3_final_backend.admin.command.domain.dto;

public class KakaoFeignClientRequestDTO {

    // Kakao 친구 메세지 전송
    public record sendKakaoMessageDTO(
            String receiver_uuids,
            String template_id
    ) {
    }
}
