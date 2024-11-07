package ticketaka.mtvs3_final_backend.sticker.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sticker_tb")
public class Sticker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long concertId;

    @Column(nullable = false)
    private String stickerName;
    @Column(nullable = false)
    private String stickerScript;

    @Column
    private StickerType stickerType;

    @Builder
    public Sticker(Long concertId, String stickerName, String stickerScript, StickerType stickerType) {
        this.concertId = concertId;
        this.stickerName = stickerName;
        this.stickerScript = stickerScript;
        this.stickerType = stickerType;
    }
}
