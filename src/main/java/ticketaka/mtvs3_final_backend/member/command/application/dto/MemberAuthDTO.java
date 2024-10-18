package ticketaka.mtvs3_final_backend.member.command.application.dto;

public class MemberAuthDTO {

    public record FileUploadDTO(
            String imgUrl,
            String secondPwd
    ) {
    }
}
