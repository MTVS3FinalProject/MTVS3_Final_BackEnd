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

        List<Long> memberStickerIdList = getMemberStickerIdList(memberId);

        StickerRarity currentRarity = stickerRarity;
        while (currentRarity != null) {
            List<Sticker> availableStickerList = getAvailableStickerList(concertId, stickerRarity, memberStickerIdList);

            if (!availableStickerList.isEmpty()) {
                return getRandomSticker(availableStickerList);
            }

            currentRarity = currentRarity.getLowerRarity();
        }

        throw new Exception400("더 이상 해당 공연에서 얻을 수 있는 스티커가 없습니다.");
    }

    private List<Sticker> getAvailableStickerList(Long concertId, StickerRarity stickerRarity, List<Long> memberStickerIdList) {
        List<Sticker> stickerList = getCollectionStickerList(concertId, StickerType.COLLECTION, stickerRarity);
        return stickerList.stream()
                .filter(sticker -> !memberStickerIdList.contains(sticker.getId()))
                .toList();
    }

    private List<Sticker> getCollectionStickerList(Long concertId, StickerType stickerType, StickerRarity stickerRarity) {
        return stickerQueryRepository.findAllByConcertIdAndStickerTypeAndStickerRarity(concertId, stickerType, stickerRarity);
    }

    // 회원이 소유한 Sticker Id 목록 조회
    private List<Long> getMemberStickerIdList(Long memberId) {
        return memberStickerQueryRepository.findAllByMemberId(memberId).stream()
                .map(MemberSticker::getStickerId)
                .toList();
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
