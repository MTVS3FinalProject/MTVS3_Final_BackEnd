package ticketaka.mtvs3_final_backend.mail.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;

import java.util.List;

@Repository
public interface MailQueryRepository extends JpaRepository<Mail, Long> {

    List<Mail> findAllByMemberId(Long memberId);

    Integer countAllByMemberId(Long memberId);
}
