package ticketaka.mtvs3_final_backend.sticker.member.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_sticker_tb")
public class MemberSticker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;
    @Column
    private Long stickerId;

    @Builder
    public MemberSticker(Long memberId, Long stickerId) {
        this.memberId = memberId;
        this.stickerId = stickerId;
    }
}
