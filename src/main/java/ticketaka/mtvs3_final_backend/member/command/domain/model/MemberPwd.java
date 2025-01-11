package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // TODO: 암호화 및 일치 여부 확인 도메인 로직 필요
}
