package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberStickerDataRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberStickerQueryService {

    private final MemberStickerDataRepository memberStickerDataRepository;

    public List<getMemberStickerDTO> getMemberStickerDTOList(Long memberId) {

        return memberStickerDataRepository.findByMemberId(memberId)
                .map(memberStickerData -> memberStickerData.getStickerList().stream()
                        .map(sticker -> new getMemberStickerDTO(
                                sticker.getStickerId(),
                                sticker.getStickerName(),
                                sticker.getStickerScript(),
                                sticker.getStickerRarity(),
                                sticker.getStickerImage()
                        ))
                        .toList()
                ).orElseGet(List::of);
    }
}
