package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class MemberInfo {

    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private LocalDate birth;

    public MemberInfo(String nickname, String email, LocalDate birth) {

        validateBirth(birth);

        this.nickname = nickname;
        this.email = email;
        this.birth = birth;
    }

    private void validateBirth(LocalDate birth) {
        if (birth.isAfter(LocalDate.now())) {
            throw new Exception400("생년월일은 미래에 있을 수 없습니다.");
        }
    }
}
