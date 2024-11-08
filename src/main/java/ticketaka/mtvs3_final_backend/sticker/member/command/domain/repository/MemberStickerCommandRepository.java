package ticketaka.mtvs3_final_backend.sticker.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;

@Repository
public interface MemberStickerCommandRepository extends JpaRepository<MemberSticker, Long> {
}
