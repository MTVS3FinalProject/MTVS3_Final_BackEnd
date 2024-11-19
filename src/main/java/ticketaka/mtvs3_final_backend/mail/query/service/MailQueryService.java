package ticketaka.mtvs3_final_backend.mail.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.mail.command.application.dto.MailCommandResponseDTO;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.mail.query.dto.MailQueryResponseDTO;
import ticketaka.mtvs3_final_backend.mail.query.repository.MailQueryRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MailQueryService {

    private final MailQueryRepository mailQueryRepository;

    /*
        우편 리스트 조회
     */
    public MailQueryResponseDTO.getMailListDTO getMailList(Long memberId) {

        List<Mail> mailList = mailQueryRepository.findAllByMemberId(memberId);
        List<MailQueryResponseDTO.mailDTO> mailDTOList = mailList.stream()
                .map(mail -> new MailQueryResponseDTO.mailDTO(
                        mail.getId().intValue(),
                        mail.getSubject(),
                        mail.getContent(),
                        mail.getMailCategory().toString(),
                        mail.getIsRead()
                ))
                .toList();

        return new MailQueryResponseDTO.getMailListDTO(mailDTOList);
    }
}
