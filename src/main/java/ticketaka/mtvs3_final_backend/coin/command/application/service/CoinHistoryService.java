package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinHistoryRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinHistory;
import ticketaka.mtvs3_final_backend.coin.command.domain.repository.CoinHistoryRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CoinHistoryService {

    private final CoinHistoryRepository coinHistoryRepository;

    /*
        Coin History 저장
     */
    @Transactional
    public void saveCoinHistory(CoinHistoryRequestDTO.saveCoinHistoryDTO requestDTO) {

        CoinHistory coinHistory = CoinHistory.builder()
                .memberId(requestDTO.memberId())
                .acquisitionType(requestDTO.acquisitionType())
                .coinAcquisitionId(requestDTO.coinAcquisitionId())
                .coinUsageType(requestDTO.coinUsageType())
                .build();

        coinHistoryRepository.save(coinHistory);
    }
}
