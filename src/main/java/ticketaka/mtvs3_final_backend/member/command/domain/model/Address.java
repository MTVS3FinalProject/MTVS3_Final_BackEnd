package ticketaka.mtvs3_final_backend.member.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "address_tb")
public class Address extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private String memberName;
    @Column(nullable = false)
    private int phoneNumber;
    @Column(nullable = false)
    private String address;
    @Column
    private String detail;

    @Builder
    public Address(Long memberId, String memberName, int phoneNumber, String address, String detail) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detail = detail;
    }
}
