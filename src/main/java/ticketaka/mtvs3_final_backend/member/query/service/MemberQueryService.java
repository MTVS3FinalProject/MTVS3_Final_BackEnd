package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.service.FileQueryService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTicketDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.redis.ticket.usable.domain.TicketUsable;
import ticketaka.mtvs3_final_backend.redis.ticket.usable.repository.TicketUsableRedisRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;
import ticketaka.mtvs3_final_backend.sticker.query.repository.StickerQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.service.TicketQueryService;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.service.TitleQueryService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    private final TicketQueryRepository ticketQueryRepository;
    private final AddressRepository addressRepository;
    private final TitleQueryRepository titleQueryRepository;
    private final StickerQueryRepository stickerQueryRepository;
    private final TicketUsableRedisRepository ticketUsableRedisRepository;

    /*
        최근 배송지 조회
     */
    public MemberQueryResponseDTO.getRecentMemberAddressDTO getRecentMemberAddress(Long memberId) {

        Address address = addressRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId)
                .orElse(null);

        if (address == null) {
            return new MemberQueryResponseDTO.getRecentMemberAddressDTO(
                    null,
                    null,
                    null,
                    null
            );
        }

        return new MemberQueryResponseDTO.getRecentMemberAddressDTO(
                address.getUserName(),
                address.getPhoneNumber(),
                address.getAddress(),
                address.getDetail()
        );
    }
    
    /*
        인벤토리 조회
     */
    public MemberQueryResponseDTO.getMemberInventoryDTO getMemberInventory(Long memberId) {

        // Member 조회
        getMember(memberId);

        // Title List 조회
        List<getMemberTitleDTO> memberTitleDTOList =
                titleQueryRepository.findAllByMemberId(memberId);

        // Sticker List 조회
        List<getMemberStickerDTO> memberStickerDTOList =
                stickerQueryRepository.findAllByMemberIdAndStickerTypeAndRelationType(memberId, StickerType.COLLECTION, RelationType.STICKER);
        
        // Custom Ticket List 조회
        List<getMemberTicketDTO> memberTicketDTOList =
                ticketQueryRepository.findAllByMemberIdAndRelationType(memberId);

        return new MemberQueryResponseDTO.getMemberInventoryDTO(
                memberTitleDTOList,
                memberStickerDTOList,
                memberTicketDTOList
        );
    }

    /*
        티켓 사용 권한 확인
     */
    public void checkTicketVerification(Long memberId, Long ticketId) {

        TicketUsable ticketUsable = ticketUsableRedisRepository.findById(ticketId.toString())
                .orElseThrow(() -> new Exception403("해당 티켓을 사용할 권한이 없습니다."));

        if (!ticketUsable.getMemberId().equals(memberId)) {
            throw new Exception400("해당 티켓의 소유자가 아닙니다.");
        }
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }
}
