package ticketaka.mtvs3_final_backend.mail.command.application.service;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.repository.FileQueryRepository;
import ticketaka.mtvs3_final_backend.mail.command.application.dto.MailCommandResponseDTO;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.Mail;
import ticketaka.mtvs3_final_backend.mail.command.domain.model.MailCategory;
import ticketaka.mtvs3_final_backend.mail.command.domain.repository.MailCommandRepository;
import ticketaka.mtvs3_final_backend.mail.query.repository.MailQueryRepository;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberStickerDTO;
import ticketaka.mtvs3_final_backend.redis.mailindex.domain.MailIndex;
import ticketaka.mtvs3_final_backend.redis.mailindex.repository.MailIndexRedisRepository;
import ticketaka.mtvs3_final_backend.redis.seat.postpone.domain.SeatPostpone;
import ticketaka.mtvs3_final_backend.redis.seat.postpone.repository.SeatPostponeRedisRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.query.repository.StickerQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.command.domain.model.PuzzleResult;
import ticketaka.mtvs3_final_backend.ticketing.puzzle.query.repository.PuzzleResultQueryRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;

import java.time.LocalDate;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MailCommandService {

    private final MailCommandRepository mailCommandRepository;
    private final SeatPostponeRedisRepository seatPostponeRedisRepository;
    private final PuzzleResultQueryRepository puzzleResultQueryRepository;
    private final TitleQueryRepository titleQueryRepository;
    private final StickerQueryRepository stickerQueryRepository;
    private final FileQueryRepository fileQueryRepository;
    private final MailQueryRepository mailQueryRepository;
    private final MailIndexRedisRepository mailIndexRedisRepository;

    // 좌석 접수 Mail
    @Transactional
    public void mailForSeatReception(Long memberId, String nickname, String concertName, String seatInfo) {

        // get MailIndex
        MailIndex mailIndex = mailIndexRedisRepository.findById(memberId.toString())
                .orElse(initMailIndex(memberId));
        Long newMailIndex = incrementMailIndex(mailIndex);

        // generate Subject
        String subject = generateSubject(newMailIndex, nickname, concertName, seatInfo, "좌석 접수를");

        // generate Content
        String content = subject + " 행운을 빕니다.";

        saveMail(memberId, subject, content, MailCategory.RECEIPT);
    }

    // 좌석 접수 취소 Mail
    @Transactional
    public void mailForCancelSeatReception(Long memberId, String nickname, String concertName, String seatInfo) {

        // get MAilIndex
        MailIndex mailIndex = mailIndexRedisRepository.findById(memberId.toString())
                .orElse(initMailIndex(memberId));
        Long newMailIndex = incrementMailIndex(mailIndex);

        // generate Subject
        String subject = generateSubject(newMailIndex, nickname, concertName, seatInfo, "좌석 접수 취소를");

        // generate Content
        String content = subject + " 왜죠?";

        saveMail(memberId, subject, content, MailCategory.CANCEL);
    }

    // 좌석 결제 미루기 Mail
    @Transactional
    public Mail mailForPostponeSeatReservation(Long memberId, String nickname, String concertName, String seatInfo) {

        // get MAilIndex
        MailIndex mailIndex = mailIndexRedisRepository.findById(memberId.toString())
                .orElse(initMailIndex(memberId));
        Long newMailIndex = incrementMailIndex(mailIndex);

        // generate Subject
        String subject = generateSubject(newMailIndex, nickname, concertName, seatInfo, "좌석 결제 미루기를");

        // generate Content
        String content = subject + " 24시간 내 결제를 완료하지 않을 경우 결제 권한을 잃습니다. 유의해 주시길 바랍니다.";

        return saveMail(memberId, subject, content, MailCategory.POSTPONE);
    }

    // 좌석 결제 완료 Mail
    @Transactional
    public void mailForSeatReservation(Long memberId, String nickname, String concertName, String seatInfo) {

        // get MAilIndex
        MailIndex mailIndex = mailIndexRedisRepository.findById(memberId.toString())
                .orElse(initMailIndex(memberId));
        Long newMailIndex = incrementMailIndex(mailIndex);

        // generate Subject
        String subject = generateSubject(newMailIndex, nickname, concertName, seatInfo, "좌석 결제를 ");

        // generate Content
        String content = subject + " 축하드립니다.";

        saveMail(memberId, subject, content, MailCategory.RESERVE);
    }

    // Puzzle 게임 결과 Mail
    @Transactional
    public Mail mailForPuzzleResult(Long memberId, String nickname, String concertName, int rank, String titleName, String stickerName) {

        // get MAilIndex
        MailIndex mailIndex = mailIndexRedisRepository.findById(memberId.toString())
                .orElse(initMailIndex(memberId));
        Long newMailIndex = incrementMailIndex(mailIndex);

        // generate PuzzleResultSubject
        String subject = "퍼즐 이벤트가 종료되었습니다. 기여도 순위와 보상을 확인해보세요.";

        // generate PuzzleResultSubject
        String content = generatePuzzleResultContent(nickname, concertName, rank, titleName, stickerName);

        return saveMail(memberId, subject, content, MailCategory.PUZZLE);
    }

    // Mail 생성
    public Mail saveMail(Long memberId, String subject, String content, MailCategory mailCategory) {
        Mail mail = Mail.builder()
                .memberId(memberId)
                .subject(subject)
                .content(content)
                .mailCategory(mailCategory)
                .build();
        mailCommandRepository.save(mail);
        return mail;
    }

    private MailIndex initMailIndex(Long memberId) {
        MailIndex mailIndex = MailIndex.builder()
                .id(memberId.toString())
                .mailIndex(0L)
                .build();
        return mailIndexRedisRepository.save(mailIndex);
    }

    private Long incrementMailIndex(MailIndex mailIndex) {
//        Long currentMailIndex = mailIndex.getMailIndex();
//        mailIndex.setMailIndex(currentMailIndex + 1);
//        mailIndexRedisRepository.save(mailIndex);
//        return mailIndex.getMailIndex();
        return 0l;
    }

    private String generateSubject(Long mailIndex, String nickname, String concertName, String seatInfo, String mailCategory) {
        return
                nickname + " 님이 " +
                concertName + " 의 " +
                seatInfo + " " +
                mailCategory + " 완료하였습니다.";
    }

    private String generatePuzzleResultContent(String nickname, String concertName, int rank, String titleName, String stickerName) {
        return nickname + " 님이 " +
                concertName + " 의 " +
                LocalDate.now() + " Puzzle 게임에서 " +
                rank + " 등 보상으로 칭호와 스티커를 획득하였습니다.";
    }

    // 특정 우편 조회
    @Transactional
    public MailCommandResponseDTO.readMailDTO readMail(Long memberId, Long mailId) {

        Mail mail = getMail(mailId);

        mail.setIsRead(true);
        mailCommandRepository.save(mail);

        return new MailCommandResponseDTO.readMailDTO(
                mail.getId().intValue(),
                mail.getSubject(),
                mail.getContent(),
                mail.getMailCategory().toString()
        );
    }

    @Transactional
    public MailCommandResponseDTO.readPostponeMailDTO readPostponeMail(Long mailId) {

        Mail mail = getMail(mailId);

        mail.setIsRead(true);
        mailCommandRepository.save(mail);

        SeatPostpone seatPostpone = seatPostponeRedisRepository.findById(mailId.toString())
                .orElseThrow(() -> new Exception401("좌석 결제를 미룬 상태가 아닙니다."));

        return new MailCommandResponseDTO.readPostponeMailDTO(
                seatPostpone.getConcertId().intValue(),
                seatPostpone.getSeatId().intValue()
        );
    }

    @Transactional
    public MailCommandResponseDTO.readPuzzleMailDTO readPuzzleMail(Long mailId) {

        Mail mail = getMail(mailId);

        PuzzleResult puzzleResult = getPuzzleResultByMailId(mailId);

        Title title = getTitle(puzzleResult.getTitleId());
        Sticker sticker = getSticker(puzzleResult.getStickerId());
        File stickerImg = getStickerImgUrl(puzzleResult.getStickerId());

        // DTO 변환
        MailCommandResponseDTO.getTitleDTO titleInfo = new MailCommandResponseDTO.getTitleDTO(
                title.getId().intValue(),
                title.getTitleName(),
                title.getTitleScript(),
                title.getTitleRarity().toString()
        );

        getMemberStickerDTO stickerInfo = new getMemberStickerDTO(
                sticker.getId(),
                sticker.getStickerName(),
                sticker.getStickerScript(),
                sticker.getStickerRarity(),
                stickerImg.getFileUrl()
        );

        mail.setIsRead(true);
        mailCommandRepository.save(mail);

        // 최종 반환 DTO 생성
        return new MailCommandResponseDTO.readPuzzleMailDTO(
                mail.getId().intValue(),
                mail.getSubject(),
                mail.getContent(),
                mail.getMailCategory().toString(),
                puzzleResult.getRank(),
                titleInfo,
                stickerInfo
        );
    }

    // Mail 조회
    private Mail getMail(Long mailId) {
        return mailCommandRepository.findById(mailId)
                .orElseThrow(() -> new Exception400("해당 우편은 존재하지 않습니다."));
    }

    // Puzzle Result 조회
    private PuzzleResult getPuzzleResultByMailId(Long mailId) {
        return puzzleResultQueryRepository.getPuzzleResultByMailId(mailId);
    }

    // Title 조회
    private Title getTitle(Long titleId) {
        return titleQueryRepository.findById(titleId)
                .orElse(null);
    }

    // Sticker 조회
    private Sticker getSticker(Long stickerId) {
        return stickerQueryRepository.findById(stickerId)
                .orElse(null);
    }

    // Sticker Image Url 조회
    private File getStickerImgUrl(Long stickerId) {
        return fileQueryRepository.findByRelationTypeAndRelationId(RelationType.STICKER, stickerId)
                .orElseThrow(() -> new Exception400("이미지 정보가 없는 스티커를 조회하였습니다."));
    }
}
