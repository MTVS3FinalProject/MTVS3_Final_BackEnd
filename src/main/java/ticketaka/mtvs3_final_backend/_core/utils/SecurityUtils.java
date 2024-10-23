package ticketaka.mtvs3_final_backend._core.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;

public class SecurityUtils {

    public static Long getCurrentMemberId() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(name.equals("anonymousUser")) {
            throw new Exception401("해당 유저의 ID를 찾을 수 없습니다.");
        }

        return Long.parseLong(name);
    }
}