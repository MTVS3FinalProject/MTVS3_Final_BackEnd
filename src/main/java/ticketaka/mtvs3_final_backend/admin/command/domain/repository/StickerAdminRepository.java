package ticketaka.mtvs3_final_backend.admin.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;

@Repository
public interface StickerAdminRepository extends JpaRepository<Sticker, Long> {
}
