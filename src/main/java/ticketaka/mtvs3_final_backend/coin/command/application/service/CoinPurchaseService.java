package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinPurchaseRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinUsageType;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinHistoryRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CoinPurchaseService {

    private final MemberRepository memberRepository;
    private final CoinHistoryRepository coinHistoryRepository;

    /*
        Coin 구매
     */
    @Transactional
    public void purchase(CoinPurchaseRequestDTO.purchaseDTO requestDTO, Long currentMemberId) {

        // 회원 확인
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("회원 인식이 되지 않습니다."));
    }
}
