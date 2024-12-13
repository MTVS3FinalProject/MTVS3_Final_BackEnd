package ticketaka.mtvs3_final_backend.coin.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinHistory;

@Repository
public interface CoinHistoryQueryRepository extends JpaRepository<CoinHistory, Long> {
}
