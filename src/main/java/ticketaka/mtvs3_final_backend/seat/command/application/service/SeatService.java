package ticketaka.mtvs3_final_backend.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatRequestDTO;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatResponseDTO;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.MemberSeatRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

import java.util.List;

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

        Concert concert = getConcertByConcertName(requestDTO.concertName());

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

        Concert concert = getConcertByConcertName(requestDTO.concertName());

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

    /*
        현재 회원이 접수한 모든 좌석 조회
    */
    public SeatResponseDTO.getReceptionSeatsDTO getReceptionSeats(SeatRequestDTO.getReceptionSeatsDTO requestDTO, Long currentMemberId) {

        // 1. 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        // 2. 현재 회원이 접수한 좌석 목록 조회
        List<Seat> receptionSeatList = seatRepository.findAllSeatsByMemberIdAndConcertIdAndStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );

        // 3. 좌석 정보를 DTO로 변환
        List<SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO> receptionSeatsDTOList = receptionSeatList.stream()
                .map(seat -> {
                    String concertDate = String.valueOf(concert.getConcertDate().getYear());  // 연도 문자열로 변환
                    String seatInfo = seat.getSection() + seat.getNumber();  // 좌석 정보 생성
                    String drawingTime = seat.getDrawingTime().toString();
                    int receptionMemberCount = memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(
                            concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED
                    ).intValue();  // 접수된 회원 수

                    // ReceptionSeatDTO 객체 생성
                    return new SeatResponseDTO.getReceptionSeatsDTO.ReceptionSeatDTO(
                            concertDate + seatInfo,
                            concertDate,
                            seatInfo,
                            drawingTime,
                            receptionMemberCount
                    );
                })
                .toList();

        // 4. 최종 DTO 생성 및 반환
        return new SeatResponseDTO.getReceptionSeatsDTO(receptionSeatsDTOList);
    }

    private MemberSeat newMemberSeat(Long currentMemberId, Long concertId, Long seatId) {
        return MemberSeat.builder()
                .memberId(currentMemberId)
                .concertId(concertId)
                .seatId(seatId)
                .memberSeatStatus(MemberSeatStatus.RECEIVED)
                .build();
    }

    private Concert getConcertByConcertName(String concertName) {
        return concertRepository.findByName(concertName)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    private Seat getSeat(Concert concert, String section, String number) {
        return seatRepository.findByConcertAndSectionAndNumber(concert, section, number)
                .orElseThrow(() -> new Exception400("해당 좌석은 존재하지 않습니다."));
    }
}
