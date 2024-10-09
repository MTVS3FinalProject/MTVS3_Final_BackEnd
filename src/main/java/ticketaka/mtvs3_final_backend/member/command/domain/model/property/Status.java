package ticketaka.mtvs3_final_backend.member.command.domain.model.property;

import lombok.Getter;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;

public enum Status {
    // 활성, 휴면, 탈퇴, 정지
    ACTIVE,
    DORMANT,
    DEACTIVATED,
    SUSPENDED
}