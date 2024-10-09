package ticketaka.mtvs3_final_backend._core.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

// 서버 에러
@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}