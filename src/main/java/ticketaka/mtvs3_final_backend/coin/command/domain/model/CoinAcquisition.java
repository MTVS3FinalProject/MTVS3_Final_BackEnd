package ticketaka.mtvs3_final_backend.coin.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coin_acquisition_tb")
public class CoinAcquisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private AcquisitionType acquisitionType;
    @Column
    private String name;
    @Column
    private int amount;
}
