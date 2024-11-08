package ticketaka.mtvs3_final_backend.ticketing.concert.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.service.FileQueryService;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.repository.MemberStickerCommandRepository;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.PaymentStatus;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertCommandService {

    private final StickerQueryService stickerQueryService;
    private final FileQueryService fileQueryService;

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final ConcertRepository concertRepository;
    private final SeatCommandRepository seatCommandRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final MemberStickerCommandRepository memberStickerCommandRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;

    /*
        공연장 정보 조회
     */
    public ConcertCommandResponseDTO.getConcertListDTO getConcertList() {

        // TODO: QueryDSL
        List<ConcertCommandResponseDTO.getConcertDTO> concertDTOList = concertRepository.findAll().stream()
                .map(concert -> new ConcertCommandResponseDTO.getConcertDTO(
                        concert.getId().intValue(),
                        concert.getName(),
                        getTimeDTO(concert.getConcertDate())
                ))
                .toList();

        return new ConcertCommandResponseDTO.getConcertListDTO(concertDTOList);
    }

    /*
        공연장 입장
     */
    public ConcertCommandResponseDTO.entranceConcertDTO entranceConcert(Long concertId, Long currentMemberId) {

        Member member = getMember(currentMemberId);
        Concert concert = getConcert(concertId);

        checkMemberAge(member, concert);

        // 내가 접수한 좌석 조회
        List<Seat> receptionSeatList = seatQueryRepository.findAllSeatsByMemberIdAndConcertIdAndMemberSeatStatus(
                currentMemberId, concert.getId(), MemberSeatStatus.RECEIVED
        );

        // 이외에 접수 가능한 좌석 조회
        List<Seat> availableSeatList = seatCommandRepository.findAllByConcertAndSeatStatus(concert, SeatStatus.AVAILABLE);
        List<ConcertCommandResponseDTO.SeatIdDTO> availableSeats = getSeatIdDTOList(availableSeatList, concert);

        List<ConcertCommandResponseDTO.SeatIdDTO> receptionSeats = getSeatIdDTOList(receptionSeatList, concert);

        int remainingTickets = concert.getReceptionLimit() - receptionSeats.size();

        return new ConcertCommandResponseDTO.entranceConcertDTO(
                concert.getId().intValue(),
                concert.getName(),
                getTimeDTO(concert.getConcertDate()),
                availableSeats,
                receptionSeats,
                remainingTickets
        );
    }

    /*
        Puzzle 결과 Sticker 획득
     */
    public ConcertCommandResponseDTO.acquireStickerFromPuzzleResultDTO acquireStickerFromPuzzleResult(Long memberId, Long concertId, ConcertCommandRequestDTO.acquireStickerFromPuzzleResultDTO requestDTO) {

        // Sticker Rarity 계산
        StickerRarity stickerRarity = calculateStickerRarity(requestDTO.rank());

        // Sticker 할당
        Sticker sticker = stickerQueryService.getPuzzleResult(memberId, concertId, stickerRarity);

        // Member Sticker 생성
        MemberSticker memberSticker = newMemberSticker(memberId, sticker.getId());
        memberStickerCommandRepository.save(memberSticker);

        // Sticker image 조회
        byte[] stickerImage = fileQueryService.getFileImage(RelationType.STICKER, sticker.getId());

        return new ConcertCommandResponseDTO.acquireStickerFromPuzzleResultDTO(
                sticker.getId().intValue(),
                sticker.getStickerName(),
                sticker.getStickerScript(),
                sticker.getStickerRarity().toString(),
                stickerImage
        );
    }

    /*
        예매자 정보 입력
     */
    public ConcertCommandResponseDTO.enterDeliveryAddressDTO enterDeliveryAddress(Long currentMemberId, Long concertId, Long seatId, ConcertCommandRequestDTO.enterDeliveryAddressDTO requestDTO) {

        Member member = getMember(currentMemberId);

        DrawResult drawResult = drawResultRedisRepository.findById(String.valueOf(currentMemberId))
                .orElseThrow(() -> new Exception403("해당 좌석에 대한 결제 권한이 없습니다."));

        Address address = newAddress(requestDTO, currentMemberId);

        addressRepository.save(address);

        drawResult.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        drawResultRedisRepository.save(drawResult);

        Concert concert = concertRepository.findById(drawResult.getConcertId())
                .orElseThrow(() -> new Exception400("해당 콘서트를 찾을 수 없습니다."));
        Seat seat = seatCommandRepository.findByIdAndConcert(drawResult.getSeatId(), concert)
                .orElseThrow(() -> new Exception400("해당 좌석을 찾을 수 없습니다."));

        String seatInfo = getSeatInfo(seat);
        int neededCoin = seat.getPrice() > member.getCoin() ? seat.getPrice() - member.getCoin() : 0;

        return new ConcertCommandResponseDTO.enterDeliveryAddressDTO(
                seatInfo,
                1,
                seat.getPrice(),
                member.getCoin(),
                neededCoin
        );
    }

    // Member 조회
    private Member getMember(Long currentMemberId) {
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 이름의 공연은 현재 존재하지 않습니다."));
    }

    // Address 생성
    // TODO: MemberController 로 이동할 예정
    private Address newAddress(ConcertCommandRequestDTO.enterDeliveryAddressDTO requestDTO, Long memberId) {
        return Address.builder()
                .memberId(memberId)
                .userName(requestDTO.userName())
                .phoneNumber(requestDTO.userPhoneNumber())
                .address(requestDTO.userAddress1())
                .detail(requestDTO.userAddress2())
                .build();
    }

    // MemberSticker 생성
    private MemberSticker newMemberSticker(Long memberId, Long stickerId) {
        return MemberSticker.builder()
                .memberId(memberId)
                .stickerId(stickerId)
                .build();
    }

    // SeatIdDTO 조회
    private List<ConcertCommandResponseDTO.SeatIdDTO> getSeatIdDTOList(List<Seat> seatList, Concert concert) {
        return seatList.stream()
                .map(seat -> {
                    String seatName = concert.getConcertDate().getYear() + seat.getSection() + seat.getNumber();

                    return new ConcertCommandResponseDTO.SeatIdDTO(seat.getId().intValue(), seatName, seat.getDrawingTime().toString());
                })
                .toList();
    }

    // SeatInfo Formatting
    private String getSeatInfo(Seat seat) {
        return seat.getSection() + "구역 " + seat.getNumber() + "번";
    }

    // TimeDTO 생성
    private ConcertCommandResponseDTO.timeDTO getTimeDTO(LocalDateTime localDateTime) {
        return new ConcertCommandResponseDTO.timeDTO(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.toLocalTime().toString()
        );
    }

    // 연령 확인
    private void checkMemberAge(Member member, Concert concert) {
        int memberAge = LocalDate.now().getYear() - member.getBirth().getYear();
        if (LocalDate.now().getDayOfYear() < member.getBirth().getDayOfYear()) {
            memberAge--; // 올해 생일이 아직 안 지났으면 1년을 뺀다
        }

        int ageRestriction = concert.getAgeRestriction();
        if(memberAge < ageRestriction) {
            throw new Exception400("해당 공연의 연령 제한을 충족하지 못합니다.");
        }
    }

    // Sticker Rarity 계산
    private StickerRarity calculateStickerRarity(int rank) {

        StickerRarity[] stickerRarities = StickerRarity.values();

        // rank 유효성 검사
        if (rank < 1 || rank > stickerRarities.length) {
            throw new Exception400("아쉽게도 Sticker 를 획득하지 못하였습니다.");
        }

        return stickerRarities[rank - 1];
    }
}
