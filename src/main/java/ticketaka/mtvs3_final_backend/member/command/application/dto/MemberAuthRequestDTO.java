package ticketaka.mtvs3_final_backend.member.command.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MemberAuthRequestDTO {

    // 기본 회원 가입
    public record signUpDTO(
            @NotBlank(message = "닉네임을 입력해 주세요.")
            String nickname,
            @Email(message = "올바른 이메일 주소를 입력해 주세요.")
            @NotBlank(message = "이메일을 입력해 주세요.")
            String email,
            @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "패스워드는 최소 6자 이상이어야 하며, 숫자를 포함해야 합니다.")
            String password,
            String ageRange
    ) {
    }

    // 기본 로그인
    // 회원 복구
    public record authDTO(
            @Email(message = "올바른 이메일 주소를 입력해 주세요.")
            @NotBlank(message = "이메일을 입력해 주세요.")
            String email,
            String password
    ) {
    }
}
