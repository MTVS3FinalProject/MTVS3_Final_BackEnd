package ticketaka.mtvs3_final_backend.concert.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
