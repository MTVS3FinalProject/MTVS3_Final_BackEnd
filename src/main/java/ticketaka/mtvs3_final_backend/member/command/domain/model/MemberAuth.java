package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class MemberAuth {

    @Column
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FAN'")
    private Authority authority;
    @Column
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;
    @Column
    @ColumnDefault("false")
    private Boolean bIsHost;

    public MemberAuth(Authority authority, Status status, Boolean bIsHost) {
        this.authority = authority;
        this.status = status;
        this.bIsHost = bIsHost;
    }
}
