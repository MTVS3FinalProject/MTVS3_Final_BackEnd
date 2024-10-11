package ticketaka.mtvs3_final_backend.file.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FileCategory;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_tb")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RelationType relationType;
    @Column
    private Long relationId;

    @Column
    private String fileUrl;
    @Column
    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;

    @Builder
    public File(RelationType relationType, long relationId, String fileUrl, FileCategory fileCategory) {
        this.relationType = relationType;
        this.relationId = relationId;
        this.fileUrl = fileUrl;
        this.fileCategory = fileCategory;
    }
}
