package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.service.FileQueryService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.redis.daily.background.domain.DailyBackground;
import ticketaka.mtvs3_final_backend.redis.daily.background.repository.DailyBackgroundRedisRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.service.ConcertQueryService;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.service.SeatQueryService;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.CustomTicket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.repository.TicketCustomQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCustomQueryService {

    private final ConcertQueryService concertQueryService;
    private final SeatQueryService seatQueryService;
    private final StickerQueryService stickerQueryService;
    private final FileQueryService fileQueryService;

    private final MemberQueryRepository memberQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final TicketCustomQueryRepository ticketCustomQueryRepository;

    private final DailyBackgroundRedisRepository dailyBackgroundRedisRepository;

    /*
        커스텀 티켓 목록 조회
     */
    public TicketCustomQueryResponseDTO.getCustomizableTicketListDTO getCustomizableTicketList(Long memberId) {

        // Member 조회
        getMember(memberId);
        // Ticket 조회
        List<Ticket> ticketList = getTicketList(memberId);

        // Custom 가능한 Ticket 이 없는 경우 빈 리스트 반환
        if(ticketList.isEmpty()) {
            return new TicketCustomQueryResponseDTO.getCustomizableTicketListDTO(List.of());
        }

        // ConcertIdList 조회
        Map<Long, Concert> concertMap = getConcertMap(ticketList);
        // SeatInfoList 조회
        Map<Long, String> seatInfoMap = getSeatInfoMap(ticketList);
        // Custom Ticket 조회
        Map<Long, CustomTicket> customTicketMap = getCustomTicketMap(ticketList);

        return new TicketCustomQueryResponseDTO.getCustomizableTicketListDTO(
                ticketList.stream()
                        .map(ticket -> {
                            Concert concert = concertMap.get(ticket.getConcertId());
                            String seatInfo = seatInfoMap.get(ticket.getSeatId());
                            CustomTicket customTicket = customTicketMap.get(ticket.getId());
                            byte[] ticketImage = customTicket != null ?
                                    fileQueryService.getTicketImage(RelationType.CUSTOM_TICKET, customTicket.getId()) :
                                    fileQueryService.getTicketImage(RelationType.TICKET, ticket.getId());

                            return new TicketCustomQueryResponseDTO.getTicketDTO(
                                    new TicketCustomQueryResponseDTO.ticketConcertDTO(
                                            concert.getName(),
                                            concert.getConcertDate().getYear(),
                                            concert.getConcertDate().getMonthValue(),
                                            concert.getConcertDate().getDayOfMonth(),
                                            concert.getConcertDate().toLocalTime().toString()
                                    ),
                                    seatInfo,
                                    ticket.getId(),
                                    ticketImage
                            );
                        })
                        .toList()
        );
    }

    /*
        티켓 커스텀 입장 - 스티커 정보 조회
     */
    public TicketCustomQueryResponseDTO.getTicketCustomObjectDTO getTicketCustomObject(Long memberId, Long ticketId) {

        // Member 조회
        getMember(memberId);
        // Ticket 조회
        Ticket ticket = getTicket(ticketId);

        // DailyBackgroundRefreshCount 조회
        Integer dailyBackgroundRefreshCount = getDailyBackgroundRefreshCount(memberId);

        // 해당 공연, 회원이 가진 Sticker List DTO 로 조회
        List<Sticker> stickerList = stickerQueryService.getStickerDTOList(memberId, ticket.getConcertId());
        // Sticker 에 대응하는 ImgUrl 조회
        Map<Long, byte[]> stickerImgMap = fileQueryService.getStickerImgMap(stickerList.stream()
                .map(Sticker::getId)
                .toList());

        return new TicketCustomQueryResponseDTO.getTicketCustomObjectDTO(
                dailyBackgroundRefreshCount,
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

    // 보유 Ticket List 조회
    private List<Ticket> getTicketList(Long memberId) {
        return ticketQueryRepository.findAllByMemberId(memberId);
    }

    // Custom Ticket List 조회
    private List<CustomTicket> getCustomTicketList(List<Long> ticketList) {
        return ticketCustomQueryRepository.findAllById(ticketList);
    }

    // Ticket List 로 ConcertMap 조회
    private Map<Long, Concert> getConcertMap(List<Ticket> ticketList) {
        return concertQueryService.getConcertList(
                        ticketList.stream()
                                .map(Ticket::getConcertId)
                                .distinct()
                                .toList()
                ).stream()
                .collect(Collectors.toMap(Concert::getId, concert -> concert));
    }

    // Ticket List 로 SeatInfoMap 조회
    private Map<Long, String> getSeatInfoMap(List<Ticket> ticketList) {
        return seatQueryService.getSeatInfoList(
                        ticketList.stream()
                                .map(Ticket::getSeatId)
                                .distinct()
                                .toList()
                ).stream()
                .collect(Collectors.toMap(Seat::getId, seatQueryService::formatSeatInfo));
    }

    // Ticket List 로 CustomTicketMap 조회
    private Map<Long, CustomTicket> getCustomTicketMap(List<Ticket> ticketList) {
        return getCustomTicketList(
                ticketList.stream()
                        .map(Ticket::getId)
                        .toList()
                ).stream()
                .collect(Collectors.toMap(CustomTicket::getTicketId, customTicket -> customTicket));
    }

    // DailyBackgroundRefreshCount
    private Integer getDailyBackgroundRefreshCount(Long memberId) {
        return dailyBackgroundRedisRepository.findById(String.valueOf(memberId))
                .map(dailyBackground -> DailyBackground.DAILY_BACKGROUND_GENERATION_LIMIT - dailyBackground.getRefreshCount())
                .orElse(DailyBackground.DAILY_BACKGROUND_GENERATION_LIMIT);
    }
}
