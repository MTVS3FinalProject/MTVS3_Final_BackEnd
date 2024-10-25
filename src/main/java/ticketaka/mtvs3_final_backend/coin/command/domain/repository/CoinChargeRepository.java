package ticketaka.mtvs3_final_backend.coin.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinCharge;

@Repository
public interface CoinChargeRepository extends JpaRepository<CoinCharge, Long> {

}
