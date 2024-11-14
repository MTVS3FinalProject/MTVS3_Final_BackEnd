package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.BackgroundRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.service.BackgroundCommandService;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileCommandService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.redis.daily.background.domain.DailyBackground;
import ticketaka.mtvs3_final_backend.redis.daily.background.repository.DailyBackgroundRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto.TicketCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.CustomTicket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.repository.TicketCustomCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.repository.TicketCustomQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.time.LocalDate;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCustomCommandService {

    private final FileCommandService fileCommandService;

    private final BackgroundCommandService backgroundCommandService;

    private final TicketCustomCommandRepository ticketCustomCommandRepository;

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final TicketCustomQueryRepository ticketCustomQueryRepository;

    private final DailyBackgroundRedisRepository dailyBackgroundRedisRepository;

    /*
        AI 배경 생성
     */
    @Transactional
    public TicketCustomCommandResponseDTO.generateAIBackgroundDTO generateAIBackground(Long memberId, Long ticketId) {

        // Member 조회
        getMember(memberId);
        // Ticket 조회
        Ticket ticket = getTicket(ticketId);

        // DailyBackground 조회
        DailyBackground dailyBackground = dailyBackgroundRedisRepository.findById(String.valueOf(memberId))
                .orElseGet(() -> {
                    // 없을 경우 새로운 DailyBackground 생성
                    return newDailyBackground(memberId);
                });

        // 날짜 확인
        if (!dailyBackground.getLastRefreshDate().isEqual(LocalDate.now())) {
            // 날짜가 이전이므로 새로운 DailyBackground 생성
            dailyBackground = newDailyBackground(memberId);
        }

        // 일일 갱신 횟수 제한 확인 및 차감
        countDailyBackground(dailyBackground);

        // Concert 조회
        Concert concert = getConcert(ticket.getConcertId());

        // AI 배경 생성, 저장 및 반환
        return backgroundCommandService.generateBackground(new BackgroundRequestDTO.generateBackgroundDTO(
                concert
        ));
    }

    /*
        Custom Ticket 저장
     */
    @Transactional
    public void saveCustomTicket(Long ticketId, TicketCommandRequestDTO.saveCustomTicketDTO requestDTO) {

        CustomTicket customTicket = getCustomTicket(ticketId);

        if (requestDTO.stickerIdList() != null && !requestDTO.stickerIdList().isEmpty()) {
            customTicket.getStickerIdList().clear();
            customTicket.getStickerIdList().addAll(requestDTO.stickerIdList().stream().map(Long::valueOf).toList());
        }

        if (requestDTO.backgroundId() != null) {
            customTicket.setBackgroundId(requestDTO.backgroundId().longValue());
        }

        customTicket = ticketCustomCommandRepository.save(customTicket);

        // Image 저장
        fileCommandService.saveCustomTicketImage(customTicket.getId(), requestDTO.customTicketImage());
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertQueryRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 공연을 찾을 수 없습니다."));
    }

    // Ticket 조회
    private Ticket getTicket(Long ticketId) {
        return ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception400("해당 티켓을 찾을 수 없습니다."));
    }

    // CustomTicket 조회 - TicketId
    private CustomTicket getCustomTicket(Long ticketId) {
        return ticketCustomQueryRepository.findByTicketId(ticketId)
                .orElse(new CustomTicket(ticketId));
    }

    // DailyBackground 생성
    private DailyBackground newDailyBackground(Long memberId) {
        DailyBackground newDailyBackground = new DailyBackground(
                String.valueOf(memberId),
                LocalDate.now()
        );
        dailyBackgroundRedisRepository.save(newDailyBackground);
        return newDailyBackground;
    }

    // DailyBackground 갱신 횟수 차감
    private void countDailyBackground(DailyBackground dailyBackground) {

        if (dailyBackground.getRefreshCount() == 0) {
            throw new Exception400("금일 배경 생성 횟수를 모두 사용하였습니다.");
        }
        dailyBackground.setRefreshCount(dailyBackground.getRefreshCount() - 1);
        dailyBackgroundRedisRepository.save(dailyBackground);
    }
}
