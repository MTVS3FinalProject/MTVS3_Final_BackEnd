package ticketaka.mtvs3_final_backend.title.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;

@Repository
public interface MemberTitleCommandRepository extends JpaRepository<MemberTitle, Long> {

    @Modifying
    @Query("UPDATE MemberTitle mt SET mt.isRepresentative = false WHERE mt.memberId = :memberId AND mt.isRepresentative = true")
    void clearRepresentativeTitle(@Param("memberId") Long memberId);
}
