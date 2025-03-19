package ticketaka.mtvs3_final_backend.title.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTitleQueryRepository extends JpaRepository<MemberTitle, Long> {

    @Query("SELECT mt FROM MemberTitle mt " +
            "JOIN Title t ON mt.titleId = t.id " +
            "WHERE mt.memberId = :memberId " +
            "AND t.concertId = :concertId")
    List<MemberTitle> findAllByMemberIdAndConcertId(@Param("memberId") Long memberId, @Param("concertId") Long concertId);

    Optional<MemberTitle> findByMemberIdAndIsRepresentativeTrue(Long memberId);

    Optional<MemberTitle> findByMemberIdAndTitleId(Long memberId, Long titleId);
}
