package ticketaka.mtvs3_final_backend.seat.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.memberseat.query.repository.MemberSeatQueryRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.seat.query.dto.SeatInfoResponseDTO;
import ticketaka.mtvs3_final_backend.seat.query.repository.SeatQueryRepository;

import java.time.LocalDateTime;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatInfoService {

    private final ConcertQueryRepository concertQueryRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final MemberSeatQueryRepository memberSeatQueryRepository;

    /*
        좌석 정보 조회
     */
    public Object getSeatInfo(Long concertId, Long seatId) {

        // Concert 조회
        Concert concert = getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        Integer competitionRate = seat.getSeatStatus().equals(SeatStatus.AVAILABLE) ?
                getCompetitionRate(getReceptionMemberCount(concertId, seatId)) : null;

        return new SeatInfoResponseDTO.getSeatInfoDTO(
                seat.getFloor(),
                formatSeatInfo(seat),
                getTimeDTO(concert.getConcertDate()),
                getTimeDTO(seat.getDrawingTime()),
                seat.getSeatStatus().toString(),
                competitionRate
        );
    }

    // Concert 조회
    private Concert getReservingConcert(Long concertId) {
        return concertQueryRepository.findByIdAndConcertStatus(concertId, ConcertStatus.RESERVING)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    // Seat 조회
    private Seat getSeat(Long seatId) {
        return seatQueryRepository.findById(seatId)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // TimeDTO 생성
    private SeatInfoResponseDTO.timeDTO getTimeDTO(LocalDateTime localDateTime) {
        return new SeatInfoResponseDTO.timeDTO(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.toLocalTime().toString()
        );
    }

    // SeatInfo 생성
    private String formatSeatInfo(Seat seat) {
        return seat.getSection() + "구역 " + seat.getNumber() + "번";
    }

    // 해당 좌석에 접수한 회원 수 조회
    private int getReceptionMemberCount(Long concertId, Long seatId) {
        return memberSeatQueryRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, MemberSeatStatus.RECEIVED
        );
    }

    // 경쟁률 계산
    private int getCompetitionRate(int receptionMemberCount) {
        double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;
        return (int) Math.round(competitionRate);
    }
}
