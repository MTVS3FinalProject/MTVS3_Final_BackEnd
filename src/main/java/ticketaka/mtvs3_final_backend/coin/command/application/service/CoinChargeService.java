package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinChargeRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinCharge;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinChargeRepository;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinHistoryRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CoinChargeService {

    private final MemberRepository memberRepository;
    private final CoinHistoryRepository coinHistoryRepository;
    private final CoinChargeRepository coinChargeRepository;

    /*
        Coin 구매
     */
    @Transactional
    public void purchase(CoinChargeRequestDTO.coinChargeDTO requestDTO, Long currentMemberId) {

        // 회원 확인
        Member member = getMember(currentMemberId);

        // 코인 충전 상품 조회
        CoinCharge coinCharge = getCoinCharge(requestDTO.coinChargeName());
    }

    // 회원 확인
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("회원 인식이 되지 않습니다."));
    }

    // 코인 충전 상품 조회
    private CoinCharge getCoinCharge(String coinChargeName) {
        return coinChargeRepository.findByName(coinChargeName)
                .orElseThrow(() -> new Exception400("해당 상품은 존재하지 않습니다."));
    }
}
