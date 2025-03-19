package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberTitleDataRepository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberTitleQueryService {

    private final MemberTitleDataRepository memberTitleDataRepository;
    private final MemberTitleQueryRepository memberTitleQueryRepository;

    public List<getMemberTitleDTO> getMemberTitleDTOList(Long memberId) {

        Long representativeTitleId = memberTitleQueryRepository.findByMemberIdAndIsRepresentativeTrue(memberId)
                .map(MemberTitle::getTitleId)
                .orElse(null);

        return memberTitleDataRepository.findByMemberId(memberId)
                .map(memberTitleData -> memberTitleData.getTitleList().stream()
                        .map(title -> new getMemberTitleDTO(
                                title.getTitleId(),
                                title.getTitleName(),
                                title.getTitleScript(),
                                title.getTitleRarity(),
                                title.getTitleId().equals(representativeTitleId)
                        ))
                        .toList()
                ).orElseGet(List::of);
    }
}
