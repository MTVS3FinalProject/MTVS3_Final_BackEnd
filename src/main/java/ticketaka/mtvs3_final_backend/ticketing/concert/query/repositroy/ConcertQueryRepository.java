package ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;

import java.util.Optional;

@Repository
public interface ConcertQueryRepository extends JpaRepository<Concert, Long> {

    Optional<Concert> findByIdAndConcertStatus(Long concertId, ConcertStatus concertStatus);
}
