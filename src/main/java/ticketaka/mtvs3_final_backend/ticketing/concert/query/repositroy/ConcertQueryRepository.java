package ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertQueryRepository extends JpaRepository<Concert, Long> {

    Optional<Concert> findByIdAndConcertStatus(Long concertId, ConcertStatus concertStatus);

    @Query("SELECT f.fileUrl " +
            "FROM Ticket t " +
            "JOIN File f ON t.concertId = f.relationId " +
            "WHERE t.memberId = :memberId " +
            "AND f.relationType = :relationType " +
            "AND f.filePurpose = :filePurpose")
    List<String> findConcertThumbnailsByMemberId(@Param("memberId") Long memberId,
                                                 @Param("relationType") RelationType relationType,
                                                 @Param("filePurpose") FilePurpose filePurpose);
}
