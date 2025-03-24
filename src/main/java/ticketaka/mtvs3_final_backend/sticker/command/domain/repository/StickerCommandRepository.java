package ticketaka.mtvs3_final_backend.sticker.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;

import java.util.Optional;

@Repository
public interface StickerCommandRepository extends JpaRepository<Sticker, Long> {

    @Query(value = """
        SELECT s 
        FROM Sticker s
        WHERE s.stickerType = 'COLLECTION'
          AND s.concertId = :concertId
          AND s.stickerRarity = :stickerRarity
          AND s.id NOT IN (
              SELECT ms.stickerId
              FROM MemberSticker ms
              WHERE ms.memberId = :memberId
          )
        ORDER BY function('RAND')
        LIMIT 1 
    """)
    Optional<Sticker> getPuzzleResultByMemberIdAndConcertId(
            @Param("memberId") Long memberId,
            @Param("concertId") Long concertId,
            @Param("stickerRarity") StickerRarity stickerRarity
    );
}
