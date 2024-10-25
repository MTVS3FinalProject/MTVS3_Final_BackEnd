package ticketaka.mtvs3_final_backend.coin.command.application.dto;

import ticketaka.mtvs3_final_backend.coin.command.domain.model.AcquisitionType;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinUsageType;

public class CoinHistoryRequestDTO {

    public record saveCoinHistoryDTO(
            Long memberId,
            AcquisitionType acquisitionType,
            Long coinAcquisitionId,
            CoinUsageType coinUsageType
    ) {
    }
}
