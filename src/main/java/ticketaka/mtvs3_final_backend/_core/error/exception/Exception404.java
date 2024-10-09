package ticketaka.mtvs3_final_backend._core.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

// 잘못된 경로
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}