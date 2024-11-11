package ticketaka.mtvs3_final_backend.title.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleType;

import java.util.List;

@Repository
public interface TitleQueryRepository extends JpaRepository<Title, Long> {

    List<Title> findAllByTitleTypeAndConcertIdAndTitleRarity(TitleType titleType, Long concertId, TitleRarity titleRarity);
}
