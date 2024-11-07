package ticketaka.mtvs3_final_backend.sticker.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;

@Repository
public interface StickerQueryRepository extends JpaRepository<Sticker, Long> {
}
