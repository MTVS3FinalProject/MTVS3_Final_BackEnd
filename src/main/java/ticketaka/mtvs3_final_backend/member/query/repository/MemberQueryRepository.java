package ticketaka.mtvs3_final_backend.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
}
