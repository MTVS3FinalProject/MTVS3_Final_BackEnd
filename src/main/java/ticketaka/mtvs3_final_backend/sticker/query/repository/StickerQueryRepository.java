package ticketaka.mtvs3_final_backend.sticker.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;

import java.util.List;

@Repository
public interface StickerQueryRepository extends JpaRepository<Sticker, Long> {

    List<Sticker> findAllByConcertIdAndStickerType(Long concertId, StickerType stickerType);

    @Query("SELECT s FROM Sticker s " +
            "JOIN MemberSticker ms ON s.id = ms.stickerId " +
            "WHERE ms.memberId = :memberId " +
            "AND s.stickerType = :stickerType")
    List<Sticker> findAllByMemberIdAndStickerType(@Param("memberId") Long memberId,
                                                  @Param("stickerType") StickerType stickerType);

    List<Sticker> findAllByConcertIdAndStickerTypeAndStickerRarity(Long concertId, StickerType stickerType, StickerRarity stickerRarity);
}
