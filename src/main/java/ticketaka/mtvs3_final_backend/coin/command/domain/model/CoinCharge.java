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
@Table(name = "coin_charge_tb")
public class CoinCharge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private int amount;
    @Column
    private int price;

    @Builder
    public CoinCharge(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }
}
