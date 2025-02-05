package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.dao.MemberTitleData;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberTitleDataRepository;

import java.util.ArrayList;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberTitleAcquiredService {

    private final MemberTitleDataRepository memberTitleDataRepository;
    
    @Transactional
    public void handleTitleAcquired(Long memberId, Long titleId, String titleName, String titleScript, String titleRarity) {

        MemberTitleData memberTitleData = memberTitleDataRepository.findByMemberId(memberId)
                .orElse(new MemberTitleData(memberId, new ArrayList<>()));

        MemberTitleData.Title title = MemberTitleData.Title.builder()
                .titleId(titleId)
                .titleName(titleName)
                .titleScript(titleScript)
                .titleRarity(titleRarity)
                .build();

        memberTitleData.addTitle(title);

        memberTitleDataRepository.save(memberTitleData);
    }
}
