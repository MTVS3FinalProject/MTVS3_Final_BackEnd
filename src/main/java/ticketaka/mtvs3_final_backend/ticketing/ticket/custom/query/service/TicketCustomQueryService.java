package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.query.service.FileQueryService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCustomQueryService {

    private final StickerQueryService stickerQueryService;
    private final FileQueryService fileQueryService;

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;

    /*
        티켓 커스텀 가능한 공연 리스트 조회
     */
    public TicketCustomQueryResponseDTO.getCustomizableTicketListDTO getCustomizableTicketList(Long memberId) {

        // Member 조회
        getMember(memberId);
        // Ticket 조회
        List<Ticket> ticketList = getTicketList(memberId);
        
        // Custom 가능한 Ticket 이 없는 경우 빈 리스트 반환 TODO: XR 쪽에서는 ?
        if(ticketList.isEmpty()) {
            return new TicketCustomQueryResponseDTO.getCustomizableTicketListDTO(List.of());
        }
        
        // ConcertIdList 조회 TODO: 하나의 공연에 대한 커스텀 티켓은 하나만???
        Map<Long, String> concertNameMap = getConcertList(
                ticketList.stream()
                        .map(Ticket::getConcertId)
                        .distinct()
                        .toList()
                ).stream()
                .collect(Collectors.toMap(Concert::getId, Concert::getName));

        return new TicketCustomQueryResponseDTO.getCustomizableTicketListDTO(
                ticketList.stream()
                        .map(ticket -> new TicketCustomQueryResponseDTO.getTicketDTO(
                                ticket.getConcertId(),
                                concertNameMap.getOrDefault(ticket.getConcertId(), "UNKNOWN CONCERT"),
                                ticket.getId()
                        ))
                        .toList()
        );
    }

    /*
        티켓 커스텀 입장 - 스티커 정보 조회
     */
    public TicketCustomQueryResponseDTO.getTicketCustomInfoDTO getTicketCustomInfo(Long memberId, Long ticketId) {

        // Member 조회
        getMember(memberId);

        // Ticket 조회
        Ticket ticket = getTicket(ticketId);

        // 해당 공연, 회원이 가진 Sticker List DTO 로 조회
        List<Sticker> stickerList = stickerQueryService.getStickerDTOList(memberId, ticket.getConcertId());
        // Sticker 에 대응하는 ImgUrl 조회
        Map<Long, byte[]> stickerImgMap = fileQueryService.getStickerImgMap(stickerList.stream()
                .map(Sticker::getId)
                .toList());

        return new TicketCustomQueryResponseDTO.getTicketCustomInfoDTO(
                stickerList.stream()
                        .map(sticker -> new TicketCustomQueryResponseDTO.stickerDTO(
                                sticker.getId().intValue(),
                                stickerImgMap.getOrDefault(sticker.getId(), null)
                        ))
                        .toList()
        );
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Ticket 조회
    private Ticket getTicket(Long ticketId) {
        return ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception400("해당 티켓을 찾을 수 없습니다."));
    }

    // ConcertList 조회
    private List<Concert> getConcertList(List<Long> concertIdList) {
        return concertQueryRepository.findAllById(concertIdList);
    }

    // 보유 Ticket List 조회
    private List<Ticket> getTicketList(Long memberId) {
        return ticketQueryRepository.findAllByMemberId(memberId);
    }
}
