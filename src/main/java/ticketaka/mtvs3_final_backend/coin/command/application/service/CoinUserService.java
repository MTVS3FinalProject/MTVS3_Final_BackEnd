package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinUserRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinAcquisition;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinAcquisitionRepository;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinHistoryRepository;
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
    public void purchase(CoinUserRequestDTO.purchaseDTO requestDTO) {

        CoinAcquisition coinAcquisition = coinAcquisitionRepository.findByName(requestDTO.purchaseName())
                .orElseThrow(() -> new Exception400("잘못된 구매 접근입니다."));
    }
}
