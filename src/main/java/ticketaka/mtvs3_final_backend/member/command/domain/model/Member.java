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
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "password"))
    })
    private MemberPwd password;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "second_password"))
    })
    private MemberPwd secondPassword;
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
    private Member(MemberInfo memberInfo, MemberPwd password, MemberPwd secondPassword, Integer avatarData, Authority authority, Status status, Boolean host) {
        this.memberInfo = memberInfo;
        this.password = password;
        this.secondPassword = secondPassword;
        this.avatarData = avatarData;
        this.authority = authority;
        this.status = Status.ACTIVE;
        this.coin = 0;
        this.bIsHost = false;
    }

    // Member 생성
    public static Member createMember(MemberInfo memberInfo, MemberPwd password, MemberPwd secondPassword, Integer avatarData, Authority authority) {
        return Member.builder()
                .memberInfo(memberInfo)
                .password(password)
                .secondPassword(secondPassword)
                .avatarData(avatarData)
                .authority(authority)
                .build();
    }
}
