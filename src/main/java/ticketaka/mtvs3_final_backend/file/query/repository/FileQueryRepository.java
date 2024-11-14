package ticketaka.mtvs3_final_backend.file.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileQueryRepository extends JpaRepository<File, Long> {

    List<File> findAllByRelationTypeAndRelationIdIn(RelationType relationType, List<Long> stickerIdList);

    Optional<File> findByRelationTypeAndRelationId(RelationType relationType, Long id);

    Optional<File> findByRelationTypeAndRelationIdAndFilePurpose(RelationType relationType, Long ticketId, FilePurpose filePurpose);
}
