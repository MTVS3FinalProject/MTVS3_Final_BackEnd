package ticketaka.mtvs3_final_backend.seat.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.query.dto.SeatResponseDTO;

import java.time.LocalDateTime;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatInfoService {

    /*
        좌석 정보 조회
     */
    public SeatResponseDTO.getSeatInfoDTO getSeatInfo(Long concertId, Long seatId) {

        // Concert 조회
        Concert concert = getConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        return new SeatResponseDTO.getSeatInfoDTO(
                seat.getFloor(),
                formatSeatInfo(seat),
                getTimeDTO(concert.getConcertDate()),
                getTimeDTO(seat.getDrawingTime()),
                getCompetitionRate(getReceptionMemberCount(concertId, seatId))
        );
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    // Seat 조회
    private Seat getSeat(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }

    // TimeDTO 생성
    private SeatResponseDTO.timeDTO getTimeDTO(LocalDateTime localDateTime) {
        return new SeatResponseDTO.timeDTO(
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
        return memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, MemberSeatStatus.RECEIVED
        );
    }

    // 경쟁률 계산
    private int getCompetitionRate(int receptionMemberCount) {
        double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;
        return (int) Math.round(competitionRate);
    }
}
