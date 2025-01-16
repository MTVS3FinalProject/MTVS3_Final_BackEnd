package ticketaka.mtvs3_final_backend.title.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleType;

import java.util.List;
import java.util.Optional;

@Repository
public interface TitleQueryRepository extends JpaRepository<Title, Long> {

    List<Title> findAllByTitleTypeAndConcertIdAndTitleRarity(TitleType titleType, Long concertId, TitleRarity titleRarity);

    @Query("SELECT new ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO(" +
            "t.id, t.titleName, t.titleScript, t.titleRarity, mt.isRepresentative) " +
            "FROM Title t " +
            "JOIN MemberTitle mt ON t.id = mt.titleId " +
            "WHERE mt.memberId = :memberId")
    List<getMemberTitleDTO> findAllByMemberId(@Param("memberId") Long memberId);

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