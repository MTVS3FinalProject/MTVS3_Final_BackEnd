package ticketaka.mtvs3_final_backend.admin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminCommandRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminVerificationResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.dto.KakaoFeignClientResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.model.KakaoToken;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.KakaoTokenRepository;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileCommandService;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.TicketStatus;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminCommandService {

    private final TitleAdminCommandService titleAdminCommandService;
    private final StickerAdminCommandService stickerAdminCommandService;
    private final FileCommandService fileCommandService;
    private final KakaoAdminService kakaoAdminService;

    private final ConcertQueryRepository concertQueryRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final SeatQueryRepository seatQueryRepository;

    /*
        Title 추가
     */
    @Transactional
    public void uploadTitle(Long concertId, AdminCommandRequestDTO.uploadTitleDTO requestDTO) {

        titleAdminCommandService.saveTitle(concertId, requestDTO);
    }

    /*
        Sticker 추가
     */
    @Transactional
    public void uploadSticker(Long concertId, AdminCommandRequestDTO.uploadStickerDTO requestDTO) {

        getConcert(concertId);

        Sticker sticker = stickerAdminCommandService.saveSticker(concertId, requestDTO);
        fileCommandService.saveStickerImage(sticker.getId(), requestDTO.stickerImage());
    }

    // Concert 조회
    private void getConcert(Long concertId) {
        concertQueryRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 공연을 찾을 수 없습니다."));
    }

    /*
        Kakao Token 발급 및 저장
     */
    @Transactional
    public void saveKakaoToken(String code) {

        // Kakao Token 발급
        KakaoFeignClientResponseDTO.KakaoTokenDTO responseDTO = kakaoAdminService.getKakaoToken(code);

        log.info("Kakao token: {}", responseDTO);

        // Kakao Token 저장
        kakaoAdminService.saveKakaoToken(responseDTO);
    }


    /*
        Kakao 친구 목록 조회
     */
    public KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriendList() {

        KakaoToken kakaoToken = kakaoTokenRepository.findTopByOrderByCreatedAtDesc()
                .orElse(null);

        KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO = kakaoAdminService.getKakaoFriendList(kakaoToken);

        log.info("Kakao friend list: {}", kakaoFriendListDTO);

        return kakaoFriendListDTO;
    }

    /*
        Kakao 친구 메세지 전송
     */
    public void sendKakaoMessage(String userName) {

        kakaoTokenRepository.findTopByOrderByCreatedAtDesc()
                .ifPresent(kakaoToken -> kakaoAdminService.sendKakaoMessage(kakaoToken, userName));
    }

    /*
        티켓 검증
     */
    @Transactional
    public AdminVerificationResponseDTO.verifyTicketDTO verifyTicket(Long ticketId) {

        Ticket ticket = ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception403("해당 번호의 티켓은 존재하지 않습니다."));

        checkTicketStatus(ticket.getTicketStatus());

        Concert concert = concertQueryRepository.findById(ticket.getConcertId())
                .orElseThrow(() -> new Exception403("해당 티켓의 공연은 존재하지 않습니다."));
        Seat seat = seatQueryRepository.findById(ticket.getSeatId())
                .orElseThrow(() -> new Exception403("해당 티켓의 좌석은 존재하지 않습니다."));

        return new AdminVerificationResponseDTO.verifyTicketDTO(
                concert.getName(),
                concert.getConcertDate(),
                formatSeatInfo(seat)
        );
    }

    private void checkTicketStatus(TicketStatus ticketStatus) {
        if (ticketStatus == TicketStatus.USED) {
            throw new Exception400("이미 사용 완료된 티켓입니다.");
        }

        if (ticketStatus == TicketStatus.CANCELLED) {
            throw new Exception400("취소된 티켓입니다.");
        }
    }

    // SeatInfo 생성
    public String formatSeatInfo(Seat seat) {
        return seat.getSection() + "구역 " + seat.getNumber() + "번";
    }
}
