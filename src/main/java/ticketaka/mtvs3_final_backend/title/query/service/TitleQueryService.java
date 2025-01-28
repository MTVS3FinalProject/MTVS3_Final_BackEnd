package ticketaka.mtvs3_final_backend.title.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleType;
import ticketaka.mtvs3_final_backend.title.command.domain.repository.TitleCommandRepository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;

import java.util.List;
import java.util.Random;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TitleQueryService {

    private final TitleQueryRepository titleQueryRepository;
    private final MemberTitleQueryRepository memberTitleQueryRepository;
    private final TitleCommandRepository titleCommandRepository;

    // 현재 장착 중인 Title 조회
    public Title getMemberTitle(Long memberId) {

        MemberTitle memberTitle = memberTitleQueryRepository.findByMemberIdAndIsRepresentative(memberId, true)
                .orElse(null);

        if (memberTitle == null) {
            return null;
        }

        return titleQueryRepository.findById(memberTitle.getTitleId())
                .orElse(null);
    }

    // 회원이 소유한 Title Id 목록 조회
    private List<Long> getMemberTitleIdList(Long memberId, Long concertId) {
        return memberTitleQueryRepository.findAllByMemberIdAndConcertId(memberId, concertId).stream()
                .map(MemberTitle::getTitleId)
                .toList();
    }

    // 획득 가능한 Title 목록 조회
    private List<Title> getAvailableTitleList(Long concertId, TitleRarity titleRarity, List<Long> memberTitleList) {
        List<Title> titleList = titleQueryRepository.findAllByTitleTypeAndConcertIdAndTitleRarity(TitleType.CONCERT, concertId, titleRarity);
        return titleList.stream()
                .filter(title -> !memberTitleList.contains(title.getId()))
                .toList();
    }

    // 랜덤으로 하나 선택
    private Title getRandomTitle(List<Title> titleList) {

        if (titleList.isEmpty()) {
            throw new Exception400("더 이상 해당 공연에서 얻을 수 있는 스티커가 없습니다.");
        }

        Random random = new Random();

        return titleList.get(random.nextInt(titleList.size()));
    }
}
