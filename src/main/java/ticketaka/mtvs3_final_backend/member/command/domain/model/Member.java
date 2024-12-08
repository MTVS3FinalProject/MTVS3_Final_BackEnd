package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_tb")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String secondPwd;
    @Column
    private LocalDate birth;
    @Column
    private Integer avatarData;

    @Column
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FAN'")
    private Authority authority;
    @Column
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;

    @Setter
    @Column
    private Integer coin;
    @Column
    private Boolean bIsHost;

    @Builder
    public Member(String nickname, String email, String password, String secondPwd, LocalDate birth, Integer avatarData, Authority authority, Status status, Boolean host) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.secondPwd = secondPwd;
        this.birth = birth;
        this.avatarData = avatarData;
        this.authority = authority;
        this.status = status;
        this.coin = 0;
        this.bIsHost = host;
    }
}
