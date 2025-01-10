package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_tb")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberInfo memberInfo;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String secondPwd;
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
    @ColumnDefault("false")
    private Boolean bIsHost;

    @Builder
    private Member(MemberInfo memberInfo, String password, String secondPwd, Integer avatarData, Authority authority, Status status, Boolean host) {
        this.memberInfo = memberInfo;
        this.password = password;
        this.secondPwd = secondPwd;
        this.avatarData = avatarData;
        this.authority = authority;
        this.status = Status.ACTIVE;
        this.coin = 0;
        this.bIsHost = false;
    }

    // Member 생성
    public static Member createMember(MemberInfo memberInfo, String password, String secondPwd, Integer avatarData, Authority authority) {
        return Member.builder()
                .memberInfo(memberInfo)
                .password(password)
                .secondPwd(secondPwd)
                .avatarData(avatarData)
                .authority(authority)
                .build();
    }
}
