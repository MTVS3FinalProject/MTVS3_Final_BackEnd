package ticketaka.mtvs3_final_backend.title.membertitle.command.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_title_tb")
public class MemberTitle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Long titleId;

    @Setter
    @Column(nullable = false)
    private Boolean isRepresentative;

    @Builder
    public MemberTitle(Long id, Long memberId, Long titleId) {
        this.id = id;
        this.memberId = memberId;
        this.titleId = titleId;
        this.isRepresentative = false;
    }
}
