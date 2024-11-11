package ticketaka.mtvs3_final_backend.title.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTitleQueryRepository extends JpaRepository<MemberTitle, Long> {

    List<MemberTitle> findAllByMemberId(Long memberId);

    Optional<MemberTitle> findByMemberIdAndIsRepresentative(Long memberId, boolean b);

    Optional<MemberTitle> findByMemberIdAndTitleId(Long memberId, Long titleId);
}
