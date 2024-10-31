package ticketaka.mtvs3_final_backend.concert.query.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.ConcertStatus;

import java.util.Optional;

public interface ConcertQueryRepository extends JpaRepository<Concert, Long> {

    Optional<Concert> findByIdAndConcertStatus(Long concertId, ConcertStatus concertStatus);
}
