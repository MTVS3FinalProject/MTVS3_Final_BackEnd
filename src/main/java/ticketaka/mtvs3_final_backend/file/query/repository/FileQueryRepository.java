package ticketaka.mtvs3_final_backend.file.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;

@Repository
public interface FileQueryRepository extends JpaRepository<File, Long> {
}
