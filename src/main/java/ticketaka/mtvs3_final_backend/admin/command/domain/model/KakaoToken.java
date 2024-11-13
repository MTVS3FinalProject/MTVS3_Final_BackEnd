package ticketaka.mtvs3_final_backend.admin.command.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketaka.mtvs3_final_backend.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kakao_token_tb")
public class KakaoToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tokenType;
    @Column
    private String accessToken;
    @Column
    private Long expiresIn;
    @Column
    private String refreshToken;
    @Column
    private Long refreshTokenExpiresIn;

    @Builder
    public KakaoToken(String tokenType, String accessToken, Long expiresIn, String refreshToken, Long refreshTokenExpiresIn) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
