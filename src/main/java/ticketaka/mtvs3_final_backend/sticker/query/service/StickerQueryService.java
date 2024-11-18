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
import ticketaka.mtvs3_final_backend.sticker.member.command.domain.model.MemberSticker;
import ticketaka.mtvs3_final_backend.sticker.member.query.repository.MemberStickerQueryRepository;
import ticketaka.mtvs3_final_backend.sticker.query.repository.StickerQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StickerQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final ConcertQueryRepository concertQueryRepository;
    private final StickerQueryRepository stickerQueryRepository;
    private final MemberStickerQueryRepository memberStickerQueryRepository;

    // Sticker 할당
    public Sticker getPuzzleResult(Long memberId, Long concertId, StickerRarity stickerRarity) {

        List<Sticker> stickerList = getCollectionStickerList(concertId, StickerType.COLLECTION, stickerRarity);
        List<Long> memberStickerIdList = memberStickerQueryRepository.findAllByMemberId(memberId).stream()
                .map(MemberSticker::getStickerId)
                .toList();

        List<Sticker> availableStickerList = stickerList.stream()
                .filter(sticker -> !memberStickerIdList.contains(sticker.getId()))
                .toList();

        return getRandomSticker(availableStickerList);
    }

    private List<Sticker> getCollectionStickerList(Long concertId, StickerType stickerType, StickerRarity stickerRarity) {
        return stickerQueryRepository.findAllByConcertIdAndStickerTypeAndStickerRarity(concertId, stickerType, stickerRarity);
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

    // 랜덤으로 하나 선택
    private Sticker getRandomSticker(List<Sticker> stickerList) {

        if (stickerList.isEmpty()) {
            throw new Exception400("더 이상 해당 공연에서 얻을 수 있는 스티커가 없습니다.");
        }

        Random random = new Random();

        return stickerList.get(random.nextInt(stickerList.size()));
    }
}
