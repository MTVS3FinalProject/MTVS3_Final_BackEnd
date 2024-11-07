package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.query.service.FileQueryService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.member.title.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.member.title.command.domain.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;
import ticketaka.mtvs3_final_backend.sticker.member.query.repository.MemberStickerQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.query.service.TitleQueryService;

import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final TitleQueryService titleQueryService;
    private final StickerQueryService stickerQueryService;
    private final FileQueryService fileQueryService;

    private final MemberTitleQueryRepository memberTitleQueryRepository;
//    private final MemberStickerQueryRepository;

    public MemberQueryResponseDTO.getMemberInventoryDTO getMemberInventory(Long memberId) {

        // Member 조회
        getMember(memberId);
        
        // Title List 조회
        List<MemberTitle> memberTitleList = memberTitleQueryRepository.findAllByMemberId(memberId);
        Map<Long, Title> titleMap = titleQueryService.getMemberTitleMap(memberTitleList);
        List<MemberQueryResponseDTO.getMemberTitleDTO> memberTitleDTOList = memberTitleList.stream()
                .map(memberTitle -> {
                    Title title = titleMap.get(memberTitle.getTitleId());

                    return new MemberQueryResponseDTO.getMemberTitleDTO(
                            title.getId().intValue(),
                            title.getTitleName(),
                            title.getTitleScript(),
                            title.getTitleRarity().toString(),
                            memberTitle.getIsRepresentative()
                    );
                })
                .toList();

        // Sticker List 조회
//        List<MemberSticker> memberStickerList = memberStickerQueryRepository.findAllByMemberId(memberId);
        List<Sticker> stickerList = stickerQueryService.getMemberStickerList(memberId);
        // Sticker 에 대응하는 ImgUrl 조회
        Map<Long, byte[]> stickerImgMap = fileQueryService.getStickerImgMap(stickerList.stream()
                .map(Sticker::getId)
                .toList());
        List<MemberQueryResponseDTO.getMemberStickerDTO> memberStickerDTOList = stickerList.stream()
                .map(sticker -> new MemberQueryResponseDTO.getMemberStickerDTO(
                        sticker.getId().intValue(),
                        sticker.getStickerName(),
                        sticker.getStickerScript(),
                        sticker.getStickerRarity().toString(),
                        stickerImgMap.getOrDefault(sticker.getId(), null)
                ))
                .toList();
        
        // Custom Ticket List 조회

        return new MemberQueryResponseDTO.getMemberInventoryDTO(
                memberTitleDTOList,
                memberStickerDTOList
        );
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }
}
