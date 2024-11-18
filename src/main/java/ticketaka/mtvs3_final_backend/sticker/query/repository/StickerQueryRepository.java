package ticketaka.mtvs3_final_backend.sticker.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.stickerDTO;

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

    @Query("SELECT new ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO(" +
            "s.id, s.stickerName, s.stickerScript, s.stickerRarity, f.fileUrl) " +
            "FROM Sticker s " +
            "JOIN MemberSticker ms ON s.id = ms.stickerId " +
            "LEFT JOIN File f ON f.relationType = :relationType AND f.relationId = s.id " +
            "WHERE ms.memberId = :memberId AND s.stickerType = :stickerType")
    List<getMemberStickerDTO> findAllByMemberIdAndStickerTypeAndRelationType(@Param("memberId") Long memberId,
                                                                             @Param("stickerType") StickerType stickerType,
                                                                             @Param("relationType") RelationType relationType);


    @Query("SELECT new ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.stickerDTO(" +
            "s.id, f.fileUrl) " +
            "FROM Sticker s " +
            "JOIN Concert c ON s.concertId = c.id " +
            "LEFT JOIN File f ON f.relationType = :relationType AND f.relationId = s.id " +
            "WHERE c.id = :concertId AND s.stickerType = :stickerType")
    List<stickerDTO> findAllByConcertId(@Param("concertId") Long concertId,
                                        @Param("stickerType") StickerType stickerType,
                                        @Param("relationType") RelationType relationType);

    @Query("SELECT new ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.stickerDTO(" +
            "s.id, f.fileUrl) " +
            "FROM Sticker s " +
            "JOIN MemberSticker ms ON s.id = ms.stickerId " +
            "LEFT JOIN File f ON f.relationType = :relationType AND f.relationId = s.id " +
            "WHERE ms.memberId = :memberId AND s.stickerType = :stickerType")
    List<stickerDTO> findAllByMemberId(@Param("memberId") Long memberId,
                                       @Param("stickerType") StickerType stickerType,
                                       @Param("relationType") RelationType relationType);
}
