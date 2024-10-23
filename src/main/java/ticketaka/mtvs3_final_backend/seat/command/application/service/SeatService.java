package ticketaka.mtvs3_final_backend.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.PaymentStatus;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.seat.command.application.dto.SeatDTO;
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
    private final MemberRepository memberRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;

    /*
        좌석 조회
     */
    public SeatResponseDTO.getSeatDTO getSeat(SeatRequestDTO.seatIdDTO requestDTO) {

        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatId(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        // 현재 좌석에 접수한 총 인원 조회
        int receptionMemberCount = memberSeatRepository.countByConcertIdAndSeatIdAndMemberSeatStatus(concert.getId(), seat.getId(), MemberSeatStatus.RECEIVED).intValue();
        // double competitionRate = receptionMemberCount > 0 ? ((double) 1 / receptionMemberCount) * 100 : 0;

        return new SeatResponseDTO.getSeatDTO(
                requestDTO.seatId(),
                seatId.section() + seatId.number(),
                seat.getDrawingTime().toString(),
                receptionMemberCount
        );
    }

    /*
        좌석 접수
     */
    @Transactional
    public SeatResponseDTO.seatReceptionDTO seatReception(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatId(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

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

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        // 현재 회원이 접수한 좌석 목록 조회
        List<Seat> receptionSeatList = seatRepository.findAllSeatsByMemberIdAndConcertIdAndStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );

        // 좌석 정보를 DTO 로 변환
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

        // 최종 DTO 생성 및 반환
        return new SeatResponseDTO.getReceptionSeatsDTO(receptionSeatsDTOList);
    }

    /*
        좌석 접수 취소
     */
    @Transactional
    public SeatResponseDTO.cancelReceptionSeatDTO cancelReceptionSeat(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatId(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        MemberSeat memberSeat = getMemberSeat(currentMemberId, concert.getId(), seat.getId());

        memberSeatRepository.delete(memberSeat);

        int receptionSeatCount = memberSeatRepository.countByMemberIdAndConcertId(currentMemberId, concert.getId());

        return new SeatResponseDTO.cancelReceptionSeatDTO(
                concert.getReceptionLimit() - receptionSeatCount
        );
    }

    /*
        추첨 시작 알림
     */
    public SeatResponseDTO.drawingNotificationDTO drawingNotification(SeatRequestDTO.seatIdDTO requestDTO) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatId(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        List<Member> memberList = memberRepository.findByConcertIdAndSeatId(
                concert.getId(), seat.getId(), MemberSeatStatus.RESERVED
        );

        List<String> nicknameList = memberList.stream()
                .map(Member::getNickname)
                .toList();

        return new SeatResponseDTO.drawingNotificationDTO(nicknameList);
    }

    /*
        좌석 추첨 결과 반영
     */
    public void createDrawResult(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // 공연 조회
        Concert concert = getConcertByConcertName(requestDTO.concertName());

        SeatDTO.getSeatId seatId = getSeatId(requestDTO.seatId());

        Seat seat = getSeat(concert, seatId.section(), seatId.number());

        // 임시 결제 권한 획득
        DrawResult drawResult = DrawResult.builder()
                .id(String.valueOf(currentMemberId))
                .concertId(concert.getId())
                .seatId(seat.getId())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        drawResultRedisRepository.save(drawResult);
    }

    /*
        좌석 결제
     */
    @Transactional
    public SeatResponseDTO.reserveSeatDTO reserveSeat(SeatRequestDTO.seatIdDTO requestDTO, Long currentMemberId) {

        // 좌석 결제 권한 확인

        // 배송지 정보 조회

        // 좌석 결제

        // 티켓 생성

        return null;
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

    private MemberSeat getMemberSeat(Long currentMemberId, Long concertId, Long seatId) {
        return memberSeatRepository.findByMemberIdAndConcertIdAndSeatId(currentMemberId, concertId, seatId)
                .orElseThrow(() -> new Exception400("해당 좌석을 접수한 내역을 찾을 수 없습니다."));
    }

    private SeatDTO.getSeatId getSeatId(String seatId) {
        return new SeatDTO.getSeatId(
                seatId.substring(4, 6),
                seatId.substring(6)
        );
    }
}
