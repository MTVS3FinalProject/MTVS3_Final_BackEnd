package ticketaka.mtvs3_final_backend._core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status.value()));
    }

    public static <T> ApiResult<T> error(T data) {
        return new ApiResult<>(false, null, data);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApiResult<T> {
        private final boolean success;
        private final T response;
        private final T error;
    }

    @Getter @Setter @AllArgsConstructor
    public static class ApiError {
        private final String message;
        private final int status;
    }
}
