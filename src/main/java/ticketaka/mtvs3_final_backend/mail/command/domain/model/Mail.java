package ticketaka.mtvs3_final_backend.mail.command.domain.model;

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

    @Column
    @Enumerated(EnumType.STRING)
    private MailCategory mailCategory;
    @Setter
    @Column
    private Boolean isRead;

    @Builder
    public Mail(Long memberId, String subject, String content, MailCategory mailCategory) {
        this.memberId = memberId;
        this.subject = subject;
        this.content = content;
        this.mailCategory = mailCategory;
        this.isRead = false;
    }
}
