package ticketaka.mtvs3_final_backend.ticketing.seat.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository.MemberSeatQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.dto.SeatQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final MemberSeatQueryRepository memberSeatQueryRepository;

    /*
        좌석 정보 조회
     */
    public SeatQueryResponseDTO.getSeatInfoDTO getConcertSeat(Long memberId, Long concertId, Long seatId) {

        // Concert 조회
        Concert concert = getReservingConcert(concertId);
        // Seat 조회
        Seat seat = getSeat(seatId);

        int receptionMemberCount = getReceptionMemberCount(concertId, seat.getId());
        return new SeatQueryResponseDTO.getSeatInfoDTO(
                seat.getFloor(),
                formatSeatInfo(seat),
                isSeatReceivedByMember(memberId, concertId, seatId),
                getTimeDTO(concert.getConcertDate()),
                getTimeDTO(seat.getDrawingTime()),
                seat.getSeatStatus().toString(),
                seat.getSeatStatus().equals(SeatStatus.AVAILABLE) ?
                        getCompetitionRate(receptionMemberCount) : null
        );
    }

    /*
        현재 회원이 접수한 모든 좌석 조회
    */
    public SeatQueryResponseDTO.getMyConcertReceptionsDTO getMyConcertReceptions(Long concertId, Long memberId) {

        // Concert 조회
        Concert concert = getReservingConcert(concertId);

        // 현재 회원이 접수한 좌석 목록 조회
        List<Seat> receptionSeatList = getReceptionSeatsForConcert(memberId, concertId);

        return new SeatQueryResponseDTO.getMyConcertReceptionsDTO(
                receptionSeatList.stream()
                        .map(seat -> {
                            int receptionMemberCount = getReceptionMemberCount(concertId, seat.getId());
                            return new SeatQueryResponseDTO.receptionSeatDTO(
                                    seat.getId().intValue(),
                                    formatSeatName(concert, seat),
                                    formatSeatInfo(seat),
                                    getTimeDTO(concert.getConcertDate()),
                                    getTimeDTO(seat.getDrawingTime()),
                                    getCompetitionRate(receptionMemberCount)
                            );
                        })
                        .toList()
        );
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
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

    // MemberSeat 조회
    private MemberSeat getMemberSeat(Long memberId, Long concertId, Long seatId) {
        return memberSeatQueryRepository.findByMemberIdAndConcertIdAndSeatId(memberId, concertId, seatId)
                .orElseThrow(() -> new Exception400("해당 좌석과 관련이 없습니다."));
    }

    // 이미 접수한 좌석인지 확인
    private boolean isSeatReceivedByMember(Long memberId, Long concertId, Long seatId) {
        return getMemberSeat(memberId, concertId, seatId).getMemberSeatStatus().equals(MemberSeatStatus.RECEIVED);
    }

    // Member 가 해당 Concert 에서 접수한 Seat 목록 조회
    private List<Seat> getReceptionSeatsForConcert(Long memberId, Long concertId) {
        return seatQueryRepository.findAllSeatsByMemberIdAndConcertIdAndMemberSeatStatus(
                memberId, concertId, MemberSeatStatus.RECEIVED
        );
    }

    // TimeDTO 생성
    private SeatQueryResponseDTO.timeDTO getTimeDTO(LocalDateTime localDateTime) {
        return new SeatQueryResponseDTO.timeDTO(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.toLocalTime().toString()
        );
    }

    // SeatName 생성
    private String formatSeatName(Concert concert, Seat seat) {
        return concert.getConcertDate().getYear() + seat.getSection() + seat.getNumber();
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
