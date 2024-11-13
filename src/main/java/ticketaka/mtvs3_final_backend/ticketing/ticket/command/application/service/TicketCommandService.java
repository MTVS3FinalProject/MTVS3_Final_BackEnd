package ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.service.QRCommandService;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto.TicketCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.application.dto.TicketResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.TicketStatus;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.repository.TicketCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service.TicketCustomCommandService;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.CustomTicket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.UUID;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCommandService {

    private final TicketCustomCommandService ticketCustomCommandService;
    private final QRCommandService qrCommandService;

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final TicketCommandRepository ticketCommandRepository;
    private final TicketQueryRepository ticketQueryRepository;

    /*
        티켓 생성
     */
    @Transactional
    public TicketResponseDTO.createTicketDTO createTicket(Long memberId, Long concertId, Long seatId) {

        getMember(memberId);
        getReservingConcert(concertId);
        Seat seat = getSeat(seatId);

        String ticketNumber = generateTicketNumber();
        Integer ticketPrice = calculateTicketPrice(seat.getPrice());

        Ticket ticket = newTicket(memberId, concertId, seatId, ticketNumber, ticketPrice);

        return new TicketResponseDTO.createTicketDTO(
                ticket.getId()
        );
    }

    /*
        Custom Ticket 저장
     */
    @Transactional
    public void saveCustomTicket(Long memberId, Long ticketId, TicketCommandRequestDTO.saveCustomTicketDTO requestDTO) {

        getMember(memberId);
        getTicket(ticketId);

        ticketCustomCommandService.saveCustomTicket(ticketId, requestDTO);
    }

    // Ticket 생성
    private Ticket newTicket(Long memberId, Long concertId, Long seatId, String ticketNumber, Integer ticketPrice) {

        Ticket ticket = Ticket.builder()
                .memberId(memberId)
                .concertId(concertId)
                .seatId(seatId)
                .ticketNumber(ticketNumber)
                .ticketPrice(ticketPrice)
                .barcodeImage(qrCommandService.generateBarcodeImage(memberId, concertId, seatId, TicketStatus.RESERVE))
                .build();

        return ticketCommandRepository.save(ticket);
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getReservingConcert(Long concertId) {
        return concertQueryRepository.findByIdAndConcertStatus(concertId, ConcertStatus.RESERVING)
                .orElseThrow(() -> new Exception400("해당 콘서트는 현재 예약 가능한 상태가 아닙니다."));
    }

    // Seat 조회
    private Seat getSeat(Long seatId) {
        return seatQueryRepository.findById(seatId)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // Ticket 조회
    private Ticket getTicket(Long ticketId) {
        return ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception400("해당 티켓은 존재하지 않습니다."));
    }

    // TicketNumber 생성
    private String generateTicketNumber() {
        return "TICKET-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase();
    }

    // Ticket Price 계산
    private Integer calculateTicketPrice(Integer seatPrice) {
        return seatPrice;
    }
}
