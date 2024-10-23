package ticketaka.mtvs3_final_backend.concert.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertRequestDTO;
import ticketaka.mtvs3_final_backend.concert.command.application.dto.ConcertResponseDTO;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;

    /*
        공연장 정보 조회
     */
    public ConcertResponseDTO.getConcertListDTO getConcertList() {

        List<Concert> concertList = concertRepository.findAll();
        List<ConcertResponseDTO.getConcertDTO> concertDTOList = concertList.stream()
                .map(concert -> new ConcertResponseDTO.getConcertDTO(
                        concert.getName(),
                        concert.getConcertDate().getYear(),
                        concert.getConcertDate().getMonthValue(),
                        concert.getConcertDate().getDayOfMonth(),
                        concert.getConcertDate().toLocalTime().toString()
                ))
                .toList();

        return new ConcertResponseDTO.getConcertListDTO(concertDTOList);
    }

    /*
        공연장 입장
     */
    public ConcertResponseDTO.entranceConcertDTO entranceConcert(ConcertRequestDTO.entranceConcertDTO requestDTO, Long currentMemberId) {

        Concert concert = concertRepository.findByName(requestDTO.concertName())
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));

        List<Seat> availableSeatList = seatRepository.findAllByConcertAndSeatStatus(concert, SeatStatus.AVAILABLE);
        List<ConcertResponseDTO.SeatIdDTO> availableSeats = getSeatIdDTOList(availableSeatList, concert);

        // 내가 접수한 좌석 조회
        List<Seat> receptionSeatList = seatRepository.findAllSeatsByMemberIdAndConcertIdAndStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );
        List<ConcertResponseDTO.SeatIdDTO> receptionSeats = getSeatIdDTOList(receptionSeatList, concert);

        int remainingTickets = concert.getReceptionLimit() - receptionSeats.size();

        return new ConcertResponseDTO.entranceConcertDTO(
                concert.getName(),
                concert.getConcertDate().getYear(),
                concert.getConcertDate().getMonthValue(),
                concert.getConcertDate().getDayOfMonth(),
                concert.getConcertDate().toLocalTime().toString(),
                availableSeats,
                receptionSeats,
                remainingTickets
        );
    }

    /*
        예매자 정보 입력
     */
    public ConcertResponseDTO.enterDeliveryAddressDTO enterDeliveryAddress(ConcertRequestDTO.enterDeliveryAddressDTO requestDTO, Long currentMemberId) {

        Member member = getMember(currentMemberId);

        DrawResult drawResult = drawResultRedisRepository.findById(String.valueOf(currentMemberId))
                .orElseThrow(() -> new Exception403("해당 좌석에 대한 결제 권한이 없습니다."));

        Address address = newAddress(requestDTO, currentMemberId);

        addressRepository.save(address);

        Concert concert = concertRepository.findById(drawResult.getConcertId())
                .orElseThrow(() -> new Exception400("해당 콘서트를 찾을 수 없습니다."));
        Seat seat = seatRepository.findByIdAndConcert(drawResult.getSeatId(), concert)
                .orElseThrow(() -> new Exception400("해당 좌석을 찾을 수 없습니다."));

        // TODO: Coin 관련 수정
        return new ConcertResponseDTO.enterDeliveryAddressDTO(
                seat.getSection() + seat.getNumber(),
                seat.getPrice(),
                10000,
                2500
        );
    }

    private Member getMember(Long currentMemberId) {
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    private Address newAddress(ConcertRequestDTO.enterDeliveryAddressDTO requestDTO, Long memberId) {
        return Address.builder()
                .memberId(memberId)
                .userName(requestDTO.userName())
                .phoneNumber(requestDTO.userPhoneNumber())
                .address(requestDTO.userAddress1())
                .detail(requestDTO.userAddress2())
                .build();
    }

    private static List<ConcertResponseDTO.SeatIdDTO> getSeatIdDTOList(List<Seat> seatList, Concert concert) {
        return seatList.stream()
                .map(seat -> {
                    String year = String.valueOf(concert.getConcertDate().getYear());
                    String seatId = year + seat.getSection() + seat.getNumber();

                    return new ConcertResponseDTO.SeatIdDTO(seatId, seat.getDrawingTime().toString());
                })
                .toList();
    }
}
