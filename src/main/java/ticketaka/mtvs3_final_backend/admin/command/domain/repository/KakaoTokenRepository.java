package ticketaka.mtvs3_final_backend.admin.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.admin.command.domain.model.KakaoToken;

@Repository
public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
}
