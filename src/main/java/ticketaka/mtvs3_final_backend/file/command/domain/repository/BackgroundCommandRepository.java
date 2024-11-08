package ticketaka.mtvs3_final_backend.file.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.Background;

@Repository
public interface BackgroundCommandRepository extends JpaRepository<Background, Long> {
}
