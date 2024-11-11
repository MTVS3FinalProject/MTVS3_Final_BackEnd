package ticketaka.mtvs3_final_backend.title.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "title_tb")
public class Title extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TitleType titleType;
    @Column
    private Long concertId;

    @Column(nullable = false, unique = true)
    private String titleName;
    @Column(nullable = false)
    private String titleScript;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TitleRarity titleRarity;

    @Builder
    public Title(TitleType titleType, Long concertId, String titleName, String titleScript, TitleRarity titleRarity) {
        this.titleType = titleType;
        this.concertId = concertId;
        this.titleName = titleName;
        this.titleScript = titleScript;
        this.titleRarity = titleRarity;
    }
}
