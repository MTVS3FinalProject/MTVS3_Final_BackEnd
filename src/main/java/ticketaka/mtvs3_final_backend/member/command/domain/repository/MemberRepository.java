package ticketaka.mtvs3_final_backend.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}