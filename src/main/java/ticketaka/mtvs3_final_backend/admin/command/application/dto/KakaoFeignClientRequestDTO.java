package ticketaka.mtvs3_final_backend.admin.command.application.dto;

public class KakaoFeignClientRequestDTO {

    public record KakaoTokenDTO(
            String grantType,
            String clientId,
            String redirectUrl,
            String code
    ) {
    }
}
