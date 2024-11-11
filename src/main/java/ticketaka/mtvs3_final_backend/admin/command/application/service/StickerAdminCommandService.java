package ticketaka.mtvs3_final_backend.admin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminCommandRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.StickerAdminCommandRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StickerAdminCommandService {

    private final StickerAdminCommandRepository stickerAdminCommandRepository;

    /*
        Sticker 추가
     */
    public Sticker saveSticker(Long concertId, AdminCommandRequestDTO.uploadStickerDTO requestDTO) {
        return stickerAdminCommandRepository.save(newSticker(concertId, requestDTO));
    }

    // Sticker 생성
    private static Sticker newSticker(Long concertId, AdminCommandRequestDTO.uploadStickerDTO requestDTO) {
        return Sticker.builder()
                .concertId(concertId)
                .stickerName(requestDTO.stickerName())
                .stickerScript(requestDTO.stickerScript())
                .stickerType(StickerType.fromString(requestDTO.stickerType()))
                .stickerRarity(StickerRarity.fromString(requestDTO.stickerRarity()))
                .build();
    }
}
