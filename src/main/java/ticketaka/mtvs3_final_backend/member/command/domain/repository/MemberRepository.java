package ticketaka.mtvs3_final_backend.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
