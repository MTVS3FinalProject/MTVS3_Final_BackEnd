package ticketaka.mtvs3_final_backend.file.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.relationType = :relationType AND f.relationId = :relationId AND f.filePurpose = :filePurpose")
    Optional<File> findByMemberForVerification(@Param("relationType") RelationType relationType, @Param("relationId") Long relationId, @Param("filePurpose") FilePurpose filePurpose);
}
