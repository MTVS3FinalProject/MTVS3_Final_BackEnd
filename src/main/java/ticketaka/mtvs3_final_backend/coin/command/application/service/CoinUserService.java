package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinUserRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinAcquisition;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinAcquisitionRepository;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinHistoryRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CoinUserService {

    private final MemberRepository memberRepository;
    private final CoinAcquisitionRepository coinAcquisitionRepository;
    private final CoinHistoryRepository coinHistoryRepository;

    /*
        Coin 구매
     */
    @Transactional
    public void purchase(CoinUserRequestDTO.purchaseDTO requestDTO, Long currentMemberId) {

        // 회원 확인
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("회원 인식이 되지 않습니다."));

        // Coin 획득 정보 조회
        CoinAcquisition coinAcquisition = coinAcquisitionRepository.findByName(requestDTO.purchaseName())
                .orElseThrow(() -> new Exception400("잘못된 구매 접근입니다."));
    }
}
