package ticketaka.mtvs3_final_backend.title.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.member.command.domain.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;

import java.util.List;
import java.util.Map;
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
}
