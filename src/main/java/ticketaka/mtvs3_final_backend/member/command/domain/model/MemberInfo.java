package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public MemberInfo(String nickname, String email, String birth) {
        this.nickname = nickname;
        this.email = email;
        this.birth = parseAndValidateBirth(birth);
    }

    private LocalDate parseAndValidateBirth(String birth) {
        LocalDate birthDate = getLocalDateBirth(birth);
        validateBirth(birthDate);
        return birthDate;
    }

    // 생일 유효성 검사
    private void validateBirth(LocalDate birth) {
        if (birth.isAfter(LocalDate.now())) {
            throw new Exception400("생년월일은 미래에 있을 수 없습니다.");
        }
    }

    // 생일 포맷 변환
    private LocalDate getLocalDateBirth(String birth) {
        // 변환할 날짜 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // String 을 LocalDate 로 변환
        return LocalDate.parse(birth, formatter);
    }
}
