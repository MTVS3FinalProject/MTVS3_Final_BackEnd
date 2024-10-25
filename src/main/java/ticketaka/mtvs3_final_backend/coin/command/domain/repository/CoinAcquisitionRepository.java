package ticketaka.mtvs3_final_backend.coin.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinUsageType;

import java.util.Optional;

@Repository
public interface CoinAcquisitionRepository extends JpaRepository<CoinUsageType, Long> {

    Optional<CoinUsageType> findByName(String coinAcquisitionName);
}
