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
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;

import java.util.List;
import java.util.Optional;

@Repository
public interface TitleQueryRepository extends JpaRepository<Title, Long> {

    List<Title> findAllByTitleTypeAndConcertIdAndTitleRarity(TitleType titleType, Long concertId, TitleRarity titleRarity);
}