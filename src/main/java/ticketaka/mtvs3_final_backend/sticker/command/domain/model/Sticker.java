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

    @Column
    private String stickerScript;

    @Column
    private StickerType stickerType;

    @Builder
    public Sticker(Long concertId, String stickerScript, StickerType stickerType) {
        this.concertId = concertId;
        this.stickerScript = stickerScript;
        this.stickerType = stickerType;
    }
}
