package ticketaka.mtvs3_final_backend.admin.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;

@Repository
public interface TitleAdminCommandRepository extends JpaRepository<Title, Long> {
}
