package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class MemberPwd {

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String secondPwd;

    public MemberPwd(String password, String secondPwd) {
        this.password = password;
        this.secondPwd = secondPwd;
    }

    // 비밀번호 일치 여부 확인
    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    // 2차 비밀번호 일치 여부 확인
    public boolean matchSecondPassword(String secondPwd, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(secondPwd, this.secondPwd);
    }
}
