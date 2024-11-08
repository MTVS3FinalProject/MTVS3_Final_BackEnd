package ticketaka.mtvs3_final_backend.sticker.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;

import java.util.List;

@Repository
public interface MemberStickerQueryRepository extends JpaRepository<MemberSticker, Long> {

    List<MemberSticker> findAllByMemberId(Long memberId);
}
