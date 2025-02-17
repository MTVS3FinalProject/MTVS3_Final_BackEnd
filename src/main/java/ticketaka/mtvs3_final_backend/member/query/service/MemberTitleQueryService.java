package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberTitleDataRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberTitleQueryService {

    private final MemberTitleDataRepository memberTitleDataRepository;

    public List<getMemberTitleDTO> getMemberTitleDTOList(Long memberId) {

        return memberTitleDataRepository.findByMemberId(memberId)
                .map(memberTitleData -> memberTitleData.getTitleList()
                        .stream()
                        .map(title -> new getMemberTitleDTO(
                                title.getTitleId(),
                                title.getTitleName(),
                                title.getTitleScript(),
                                title.getTitleRarity()
                        ))
                        .toList()
                ).orElseGet(List::of);
    }
}
