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
import ticketaka.mtvs3_final_backend.mail.command.application.service.MailCommandService;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.model.MailPuzzleResult;
import ticketaka.mtvs3_final_backend.mail.puzzle.command.domain.repository.MailPuzzleResultCommandRepository;
import ticketaka.mtvs3_final_backend.redis.ticket.address.domain.TicketAddress;
import ticketaka.mtvs3_final_backend.redis.ticket.address.repository.TicketAddressRedisRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.service.StickerAcquireService;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.repository.MemberStickerCommandRepository;
import ticketaka.mtvs3_final_backend.sticker.query.service.StickerQueryService;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.application.dto.ConcertCommandResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.DrawResult;
import ticketaka.mtvs3_final_backend.redis.drawing.domain.PaymentStatus;
import ticketaka.mtvs3_final_backend.redis.drawing.repository.DrawResultRedisRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.repository.MemberSeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.model.PuzzleResult;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.repository.PuzzleResultCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.query.repository.SeatQueryRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.service.TitleAcquireService;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.member.command.domain.repository.MemberTitleCommandRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertCommandService {

    private final StickerQueryService stickerQueryService;
    private final FileQueryService fileQueryService;

    private final MemberRepository memberRepository;
    private final ConcertRepository concertRepository;
    private final SeatCommandRepository seatCommandRepository;
    private final SeatQueryRepository seatQueryRepository;
    private final MemberStickerCommandRepository memberStickerCommandRepository;

    private final DrawResultRedisRepository drawResultRedisRepository;
    private final TicketAddressRedisRepository ticketAddressRedisRepository;
    private final MemberTitleCommandRepository memberTitleCommandRepository;
    private final PuzzleResultCommandRepository puzzleResultCommandRepository;
    private final MailCommandService mailCommandService;
    private final MailPuzzleResultCommandRepository mailPuzzleResultCommandRepository;
    private final MemberSeatCommandRepository memberSeatCommandRepository;

    private final TitleAcquireService titleAcquireService;
    private final StickerAcquireService stickerAcquireService;

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
    public ConcertCommandResponseDTO.entranceConcertDTO entranceConcert(Long concertId, Long memberId) {

        Member member = getMember(memberId);
        Concert concert = getConcert(concertId);

        checkMemberAge(member, concert);

        // 접수 가능한 좌석 조회
        List<Seat> availableSeatList = seatCommandRepository.findAllByConcertAndSeatStatus(concert, SeatStatus.AVAILABLE);
        // 이미 추첨 완료되었거나 예약된 좌석 조회
        List<Seat> reservedSeatList = seatCommandRepository.findAllByConcertAndSeatStatus(concert, SeatStatus.RESERVED);
        // 내가 접수한 좌석 조회
        List<Seat> myReceptionSeatList = seatQueryRepository.findAllSeatsByMemberIdAndConcertIdAndMemberSeatStatus(
                memberId, concert.getId(), MemberSeatStatus.RECEIVED
        );

        List<ConcertCommandResponseDTO.SeatIdDTO> availableSeats = getSeatIdDTOList(availableSeatList, concert);
        List<ConcertCommandResponseDTO.SeatIdDTO> reservedSeats = getSeatIdDTOList(reservedSeatList, concert);
        List<ConcertCommandResponseDTO.SeatIdDTO> myReceptionSeats = getSeatIdDTOList(myReceptionSeatList, concert);

        int remainingTickets = concert.getReceptionLimit() - countMySeat(memberId, concertId);

        return new ConcertCommandResponseDTO.entranceConcertDTO(
                concert.getId().intValue(),
                concert.getName(),
                getTimeDTO(concert.getConcertDate()),
                availableSeats,
                reservedSeats,
                myReceptionSeats,
                remainingTickets
        );
    }

    private Integer countMySeat(Long memberId, Long concertId) {
        return memberSeatCommandRepository.countByMemberIdAndConcertId(memberId, concertId);
    }

    /*
        Puzzle 결과 Title, Sticker 획득
     */
    @Transactional
    public ConcertCommandResponseDTO.acquireStickerFromPuzzleResultDTO acquireStickerFromPuzzleResult(Long memberId, Long concertId, ConcertCommandRequestDTO.acquireStickerFromPuzzleResultDTO requestDTO) {

        // Member 조회
        Member member = getMember(memberId);
        // Concert 조회
        Concert concert = getConcert(concertId);

        // Title 할당
        Optional<Title> optionalTitle = titleAcquireService.getTitleByPuzzleResult(memberId, concertId, requestDTO.rank());
        // Sticker 할당
        Sticker sticker = stickerAcquireService.getStickerByPuzzleResult(memberId, concertId, requestDTO.rank());

        Long titleId = optionalTitle.map(Title::getId).orElse(null);
        // PuzzleResult 저장
        PuzzleResult puzzleResult = PuzzleResult.newPuzzleResult(memberId, concertId, titleId, sticker.getId(), requestDTO.rank());

        // Mail 저장
        Mail mail = mailCommandService.mailForPuzzleResult(
                memberId,
                member.getMemberInfo().getNickname(),
                concert.getName(),
                requestDTO.rank(),
                optionalTitle.map(Title::getTitleName).orElse(null),
                sticker.getStickerName()
        );

        // MailPuzzleResult 저장
        newMailPuzzleResult(mail.getId(), puzzleResult.getId());

        // Member Title 할당
        MemberTitle memberTitle = newMemberTitle(memberId, titleId);
        memberTitleCommandRepository.save(memberTitle);
        // Member Sticker 생성
        MemberSticker memberSticker = newMemberSticker(memberId, sticker.getId());
        memberStickerCommandRepository.save(memberSticker);

        // Sticker image 조회
        String stickerImage = fileQueryService.getFileImage(RelationType.STICKER, sticker.getId());

        return new ConcertCommandResponseDTO.acquireStickerFromPuzzleResultDTO(
                optionalTitle.map(title -> new ConcertCommandResponseDTO.titleInfoDTO(
                        title.getId().intValue(),
                        title.getTitleName(),
                        title.getTitleScript(),
                        title.getTitleRarity().toString()
                )).orElse(null),
                new ConcertCommandResponseDTO.stickerInfoDTO(
                        sticker.getId().intValue(),
                        sticker.getStickerName(),
                        sticker.getStickerScript(),
                        sticker.getStickerRarity().toString(),
                        stickerImage
                )
        );
    }

    private void newMailPuzzleResult(Long mailId, Long puzzleResultId) {
        MailPuzzleResult mailPuzzleResult = MailPuzzleResult.builder()
                .mailId(mailId)
                .puzzleResultId(puzzleResultId)
                .build();
        mailPuzzleResultCommandRepository.save(mailPuzzleResult);
    }

    /*
        예매자 정보 입력
     */
    @Transactional
    public ConcertCommandResponseDTO.enterDeliveryAddressDTO enterDeliveryAddress(Long memberId, Long concertId, Long seatId, ConcertCommandRequestDTO.enterDeliveryAddressDTO requestDTO) {

        Member member = getMember(memberId);

        DrawResult drawResult = getDrawResult(memberId, concertId, seatId);

        TicketAddress ticketAddress = newTicketAddress(memberId, concertId, seatId, requestDTO);
        ticketAddressRedisRepository.save(ticketAddress);

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

    // TicketAddress 생성
    private TicketAddress newTicketAddress(Long memberId, Long concertId, Long seatId, ConcertCommandRequestDTO.enterDeliveryAddressDTO requestDTO) {
        return TicketAddress.builder()
                .id(TicketAddress.generateTicketAddressId(memberId, concertId, seatId))
                .userName(requestDTO.userName())
                .userPhoneNumber(requestDTO.userPhoneNumber())
                .userAddress1(requestDTO.userAddress1())
                .userAddress2(requestDTO.userAddress2())
                .build();
    }

    // DrawResult 조회
    private DrawResult getDrawResult(Long memberId, Long concertId, Long seatId) {
        String id = memberId + "-" + concertId + "-" + seatId;
        return drawResultRedisRepository.findById(id)
                .orElseThrow(() -> new Exception403("좌석 결제 권한이 없습니다."));
    }

    // MemberTitle 생성
    private MemberTitle newMemberTitle(Long memberId, Long titleId) {
        return MemberTitle.builder()
                .memberId(memberId)
                .titleId(titleId)
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
        int memberAge = LocalDate.now().getYear() - member.getMemberInfo().getBirth().getYear();
        if (LocalDate.now().getDayOfYear() < member.getMemberInfo().getBirth().getDayOfYear()) {
            memberAge--; // 올해 생일이 아직 안 지났으면 1년을 뺀다
        }

        int ageRestriction = concert.getAgeRestriction();
        if(memberAge < ageRestriction) {
            throw new Exception400("해당 공연의 연령 제한을 충족하지 못합니다.");
        }
    }
}
