package ticketaka.mtvs3_final_backend.title.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleType;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TitleQueryService {

    private final TitleQueryRepository titleQueryRepository;
    private final MemberTitleQueryRepository memberTitleQueryRepository;

    // Inventory Title 조회
    public Map<Long, Title> getMemberTitleMap(List<MemberTitle> memberTitleList) {

        List<Title> titleList = titleQueryRepository.findAllById(
                memberTitleList.stream()
                        .map(MemberTitle::getTitleId)
                        .toList()
        );

        return titleList.stream()
                .collect(Collectors.toMap(Title::getId, title -> title));
    }

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

    // Title 할당
    public Title getPuzzleResult(Long memberId, Long concertId, TitleRarity titleRarity) {

        List<Title> titleList = titleQueryRepository.findAllByTitleTypeAndConcertIdAndTitleRarity(TitleType.CONCERT, concertId, titleRarity);
        List<Long> memberTitleList = memberTitleQueryRepository.findAllByMemberId(memberId).stream()
                .map(MemberTitle::getTitleId)
                .toList();

        List<Title> availableTitleList = titleList.stream()
                .filter(title -> !memberTitleList.contains(title.getId()))
                .toList();

        return getRandomTitle(availableTitleList);
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
