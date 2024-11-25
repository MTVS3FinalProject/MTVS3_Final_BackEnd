package ticketaka.mtvs3_final_backend.ticketing.ticket.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.redis.daily.background.domain.DailyBackground;
import ticketaka.mtvs3_final_backend.redis.daily.background.repository.DailyBackgroundRedisRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;
import ticketaka.mtvs3_final_backend.sticker.query.repository.StickerQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.service.ConcertQueryService;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.service.SeatQueryService;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.repository.TicketCustomQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.TicketQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.stickerDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final StickerQueryRepository stickerQueryRepository;

    private final DailyBackgroundRedisRepository dailyBackgroundRedisRepository;

    /*
        보유 티켓 조회
     */
    public TicketQueryResponseDTO.getCustomizableTicketListDTO getCustomizableTicketList(Long memberId) {

        // Member 조회
        getMember(memberId);

        // Ticket 조회
        List<getTicketDTO> ticketList = ticketQueryRepository.findCustomizableTicketsByMemberId(memberId);

        // Custom 가능한 Ticket 이 없는 경우 빈 리스트 반환
        if(ticketList.isEmpty()) {
            return new TicketQueryResponseDTO.getCustomizableTicketListDTO(List.of());
        }

        return new TicketQueryResponseDTO.getCustomizableTicketListDTO(
                ticketList
        );
    }

    /*
        티켓 커스텀 입장
     */
    public TicketQueryResponseDTO.getTicketCustomObjectDTO getTicketCustomObject(Long memberId, Long ticketId) {

        // Member 조회
        getMember(memberId);
        // Ticket 조회
        Ticket ticket = getTicket(ticketId);

        // DailyBackgroundRefreshCount 조회
        Integer dailyBackgroundRefreshCount = getDailyBackgroundRefreshCount(memberId);

        // 해당 공연, 회원이 가진 Sticker List DTO 로 조회
        List<stickerDTO> stickerDTOList = new ArrayList<>();
        stickerDTOList.addAll(stickerQueryRepository.findAllByConcertId(ticket.getConcertId(), StickerType.COMMON, RelationType.STICKER));
        stickerDTOList.addAll(stickerQueryRepository.findAllByMemberId(memberId, StickerType.COLLECTION, RelationType.STICKER));

        List<getTicketDTO> getTicketDTOList = ticketQueryRepository.findCustomizableTicketsByMemberId(memberId);
        List<TicketQueryResponseDTO.ticketDTO> ticketDTOList = getTicketDTOList.stream()
                .map(ticketDTO -> new TicketQueryResponseDTO.ticketDTO(
                        ticketDTO.ticketId(),
                        ticketDTO.concertName(),
                        ticketDTO.year(),
                        ticketDTO.month(),
                        ticketDTO.day(),
                        ticketDTO.time(),
                        ticketDTO.seatInfo()
                ))
                .toList();

        return new TicketQueryResponseDTO.getTicketCustomObjectDTO(
                dailyBackgroundRefreshCount,
                stickerDTOList,
                ticketDTOList
        );
    }

    /*
        티켓 상세 정보 조회
     */
    public getTicketDTO getTicketDetails(Long memberId, Long ticketId) {

        getMember(memberId);

        return ticketQueryRepository.findTicketDTOByMemberIdAndTicketId(memberId, ticketId);
    }

    // Member 조회
    private void getMember(Long memberId) {
        memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Ticket 조회
    private Ticket getTicket(Long ticketId) {
        return ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception400("해당 티켓을 찾을 수 없습니다."));
    }

    // DailyBackgroundRefreshCount
    private Integer getDailyBackgroundRefreshCount(Long memberId) {
        return dailyBackgroundRedisRepository.findById(String.valueOf(memberId))
                .map(dailyBackground -> DailyBackground.DAILY_BACKGROUND_GENERATION_LIMIT - dailyBackground.getRefreshCount())
                .orElse(DailyBackground.DAILY_BACKGROUND_GENERATION_LIMIT);
    }
}
