package ticketaka.mtvs3_final_backend.coin.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coin_history_tb")
public class CoinHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;
    @Column
    @Enumerated(EnumType.STRING)
    private AcquisitionType acquisitionType;
    @Column
    private Long coinAcquisitionId;
    @Column
    @Enumerated(EnumType.STRING)
    private CoinUsageType coinUsageType;
    @Column
    private Integer amount;

    @Builder
    public CoinHistory(Long memberId, AcquisitionType acquisitionType, Long coinAcquisitionId, CoinUsageType coinUsageType, Integer amount) {
        this.memberId = memberId;
        this.acquisitionType = acquisitionType;
        this.coinAcquisitionId = coinAcquisitionId;
        this.coinUsageType = coinUsageType;
        this.amount = amount;
    }
}
