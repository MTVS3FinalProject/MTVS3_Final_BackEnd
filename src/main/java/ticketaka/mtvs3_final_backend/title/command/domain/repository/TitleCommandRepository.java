package ticketaka.mtvs3_final_backend.title.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

import java.util.Optional;

@Repository
public interface TitleCommandRepository extends JpaRepository<Title, Long> {

    @Query(value = """
        SELECT t
        FROM Title t
        WHERE t.titleType = 'CONCERT'
          AND t.concertId = :concertId
          AND t.titleRarity = :titleRarity
          AND t.id NOT IN (
              SELECT mt.titleId
              FROM MemberTitle mt
              WHERE mt.memberId = :memberId
          )
        ORDER BY function('RAND')
        LIMIT 1
    """)
    Optional<Title> getPuzzleResultByMemberIdAndConcertId(
            @Param("memberId") Long memberId,
            @Param("concertId") Long concertId,
            @Param("titleRarity") TitleRarity titleRarity
    );
}
