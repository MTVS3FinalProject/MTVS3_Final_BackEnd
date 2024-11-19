package ticketaka.mtvs3_final_backend.mail.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.mail.command.application.dto.MailCommandResponseDTO;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.MailCategory;
import ticketaka.mtvs3_final_backend.mail.command.domain.repository.MailCommandRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MailCommandService {

    private final MailCommandRepository mailCommandRepository;

    // 좌석 접수 Mail
    @Transactional
    public void mailForSeatReception(Long memberId, String nickname, String concertName, String seatInfo) {

        // generate Subject
        String subject = generateSubject(nickname, concertName, seatInfo, "좌석 접수를");

        // generate Content
        String content = subject + "\n행운을 빕니다.";

        saveMail(memberId, subject, content, MailCategory.RECEIPT);
    }

    // 좌석 접수 취소 Mail
    @Transactional
    public void mailForCancelSeatReception(Long memberId, String nickname, String concertName, String seatInfo) {

        // generate Subject
        String subject = generateSubject(nickname, concertName, seatInfo, "좌석 접수 취소를");

        // generate Content
        String content = subject + "\n왜죠?";

        saveMail(memberId, subject, content, MailCategory.CANCEL);
    }

    // 좌석 결제 미루기 Mail
    @Transactional
    public void mailForPostponeSeatReservation(Long memberId, String nickname, String concertName, String seatInfo) {

        // generate Subject
        String subject = generateSubject(nickname, concertName, seatInfo, "좌석 결제 미루기를");

        // generate Content
        String content = subject + "\n24시간 내 결제를 완료하지 않을 경우 결제 권한을 잃습니다.\n유의해 주시길 바랍니다.";

        saveMail(memberId, subject, content, MailCategory.POSTPONE);
    }

    // 좌석 결제 완료 Mail
    @Transactional
    public void mailForSeatReservation(Long memberId, String nickname, String concertName, String seatInfo) {

        // generate Subject
        String subject = generateSubject(nickname, concertName, seatInfo, "좌석 결제 미루기를");

        // generate Content
        String content = subject + "\n축하드립니다.";

        saveMail(memberId, subject, content, MailCategory.RESERVE);
    }

    // Mail 생성
    public void saveMail(Long memberId, String subject, String content, MailCategory mailCategory) {
        Mail mail = Mail.builder()
                .memberId(memberId)
                .subject(subject)
                .content(content)
                .mailCategory(mailCategory)
                .build();

        mailCommandRepository.save(mail);
    }

    private String generateSubject(String nickname, String concertName, String seatInfo, String mailCategory) {

        return nickname + " 님이 " +
                concertName + " 의 " +
                seatInfo + " " +
                mailCategory + " 완료하였습니다.";
    }

    /*
        우편 리스트 조회
     */
    public MailCommandResponseDTO.getMailListDTO getMailList(Long memberId) {

        List<Mail> mailList = mailCommandRepository.findAllByMemberId(memberId);
        List<MailCommandResponseDTO.mailDTO> mailDTOList = mailList.stream()
                .map(mail -> new MailCommandResponseDTO.mailDTO(
                        mail.getId().intValue(),
                        mail.getSubject(),
                        mail.getContent(),
                        mail.getMailCategory().toString(),
                        mail.getIsRead()
                ))
                .toList();

        return new MailCommandResponseDTO.getMailListDTO(mailDTOList);
    }
}
