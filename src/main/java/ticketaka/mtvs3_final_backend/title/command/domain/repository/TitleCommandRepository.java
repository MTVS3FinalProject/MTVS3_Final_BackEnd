package ticketaka.mtvs3_final_backend.title.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;

@Repository
public interface TitleCommandRepository extends JpaRepository<Title, Long> {
}
