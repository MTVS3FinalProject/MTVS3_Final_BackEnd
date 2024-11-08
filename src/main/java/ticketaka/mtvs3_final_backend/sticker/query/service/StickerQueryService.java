package ticketaka.mtvs3_final_backend.sticker.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerRarity;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.StickerType;
import ticketaka.mtvs3_final_backend.sticker.query.repository.StickerQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StickerQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final StickerQueryRepository stickerQueryRepository;

    /*
        해당 공연, 회원이 가진 Sticker List DTO 로 조회
     */
    public List<Sticker> getStickerDTOList(Long memberId, Long concertId) {

        // Member 조회
        getMember(memberId);
        // Concert 조회
        getConcert(concertId);

        // 공통 Sticker 조회 및 회원이 가진 Sticker 조회 후 합치기
        List<Sticker> stickerList = new ArrayList<>(getCommonStickerList(concertId));
        stickerList.addAll(getMemberStickerList(memberId));

        return stickerList;
    }

    // Sticker 할당
    public Sticker getPuzzleResult(Long memberId, StickerRarity stickerRarity) {

        return null;
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertQueryRepository.findByIdAndConcertStatus(concertId, ConcertStatus.RESERVING)
                .orElseThrow(() -> new Exception400("해당 콘서트는 현재 예약 가능한 상태가 아닙니다."));
    }

    // 공통 Sticker 조회
    private List<Sticker> getCommonStickerList(Long concertId) {
        return stickerQueryRepository.findAllByConcertIdAndStickerType(concertId, StickerType.COMMON);
    }

    // 회원이 가진 Sticker 조회
    public List<Sticker> getMemberStickerList(Long memberId) {
        return stickerQueryRepository.findAllByMemberIdAndStickerType(memberId, StickerType.COLLECTION);
    }
}
