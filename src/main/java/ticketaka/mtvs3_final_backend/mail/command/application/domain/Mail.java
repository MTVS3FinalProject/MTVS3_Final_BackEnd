package ticketaka.mtvs3_final_backend.mail.command.application.domain;

import jakarta.persistence.*;
import lombok.*;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mail_tb")
public class Mail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String content;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private MailStatus mailStatus;
    @Column
    @Enumerated(EnumType.STRING)
    private MailCategory mailCategory;
    @Column
    private Long targetId;

    @Builder
    public Mail(Long memberId, String subject, String content, MailStatus mailStatus, MailCategory mailCategory, Long targetId) {
        this.memberId = memberId;
        this.subject = subject;
        this.content = content;
        this.mailStatus = mailStatus;
        this.mailCategory = mailCategory;
        this.targetId = targetId;
    }
}
