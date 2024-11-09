package ticketaka.mtvs3_final_backend.title.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;

import java.util.List;

@Repository
public interface MemberTitleQueryRepository extends JpaRepository<MemberTitle, Long> {

    List<MemberTitle> findAllByMemberId(Long memberId);
}
