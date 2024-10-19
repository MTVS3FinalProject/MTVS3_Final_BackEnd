package ticketaka.mtvs3_final_backend.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.MemberSeatRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final MemberSeatRepository memberSeatRepository;

    /*
        좌석 조회
     */
    public SeatResponseDTO.getSeatDTO getSeat(SeatRequestDTO.seatIdDTO requestDTO) {

        Concert concert = getConcertByConcertName(requestDTO);

        String section = requestDTO.seatId().substring(4, 5);
        String number = requestDTO.seatId().substring(5);

        Seat seat = getSeat(concert, section, number);

        // 현재 좌석에 접수한 총 인원 조회
        int receptionMemberCount = memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED).intValue();
        // double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;

        return new SeatResponseDTO.getSeatDTO(
                requestDTO.seatId(),
                section + number,
                seat.getDrawingTime().toString(),
                receptionMemberCount
        );
    }

    /*
        좌석 접수
     */
    public SeatResponseDTO.seatReceptionDTO seatReception(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        Concert concert = getConcertByConcertName(requestDTO);

        String section = requestDTO.seatId().substring(4, 5);
        String number = requestDTO.seatId().substring(5);

        Seat seat = getSeat(concert, section, number);

        MemberSeat memberSeat = newMemberSeat(currentMemberId, concert.getId(), seat.getId());

        memberSeatRepository.save(memberSeat);

        int receptionMemberCount = memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED).intValue();
        int receptionCount = memberSeatRepository.countByMemberIdAndConcertId(currentMemberId, concert.getId());

        return new SeatResponseDTO.seatReceptionDTO(
                receptionMemberCount,
                seat.getPrice(),
                concert.getReceptionLimit() - receptionCount
        );
    }

    private MemberSeat newMemberSeat(Long currentMemberId, Long concertId, Long seatId) {
        return MemberSeat.builder()
                .memberId(currentMemberId)
                .concertId(concertId)
                .seatId(seatId)
                .memberSeatStatus(MemberSeatStatus.RECEIVED)
                .build();
    }

    private Concert getConcertByConcertName(SeatRequestDTO.seatIdDTO requestDTO) {
        return concertRepository.findByName(requestDTO.concertName())
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    private Seat getSeat(Concert concert, String section, String number) {
        return seatRepository.findByConcertAndSectionAndNumber(concert, section, number)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }
}
