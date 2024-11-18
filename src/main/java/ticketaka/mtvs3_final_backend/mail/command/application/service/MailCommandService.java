package ticketaka.mtvs3_final_backend.mail.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.MailCategory;
import ticketaka.mtvs3_final_backend.mail.command.domain.repository.MailCommandRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MailCommandService {

    private final MailCommandRepository mailCommandRepository;

    // Mail 생성
    public void sendMail(Long memberId, String subject, String content, MailCategory mailCategory) {
        Mail mail = Mail.builder()
                .memberId(memberId)
                .subject(subject)
                .content(content)
                .mailCategory(mailCategory)
                .build();

        mailCommandRepository.save(mail);
    }
}
