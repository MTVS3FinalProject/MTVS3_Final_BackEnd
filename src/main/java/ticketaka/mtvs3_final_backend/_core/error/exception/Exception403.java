package ticketaka.mtvs3_final_backend._core.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

// 권한 없음
@Getter
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}