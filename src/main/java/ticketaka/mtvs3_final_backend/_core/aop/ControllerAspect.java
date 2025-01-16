package ticketaka.mtvs3_final_backend._core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    private static final Integer ErrorStackTraceNum = 3;

    @Pointcut("execution(* ticketaka.mtvs3_final_backend..controller..*.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 메소드 정보 추출
        Method method = getMethod(proceedingJoinPoint);

        log.info("===== API 요청 : {} =====", method.getName());

        // 파라미터 추출
        Object[] args = proceedingJoinPoint.getArgs();

        if (args.length == 0) {
            log.info("No Parameter");
        }

        for (Object arg : args) {
            log.info("Parameter Type = {}", arg.getClass().getSimpleName());
            log.info("Parameter Value = {}", arg);
        }

        // 시작 시간 측정
        long startTime = System.currentTimeMillis();

        try {
            // 실제 메소드 실행
            Object response = proceedingJoinPoint.proceed(args);

            // 종료 시간 측정
            long endTime = System.currentTimeMillis();

            log.info("===== API 응답 : {} 소요 시간 : {} =====", method.getName(), (endTime - startTime));

            log.info("Response Type = {}", response.getClass().getSimpleName());

            if (response instanceof ResponseEntity<?> responseEntity) {
                Object body = responseEntity.getBody();

                if (body instanceof ApiUtils.ApiResult<?> apiResult) {
                    log.info("ApiResult Response = {}", apiResult.getResponse());
                    log.info("ApiResult Error = {}", apiResult.getError());
                } else {
                    log.info("Response Body = {}", body);
                }
            } else {
                log.info("Response Value = {}", response);
            }

            return response;

        } catch (Exception e) {

            // 종료 시간 측정
            long endTime = System.currentTimeMillis();

            log.info("===== API 응답 실패 : {} 소요 시간 : {} =====", method.getName(), (endTime - startTime));

            log.error("Exception occurred in method: {} with message: {}",
                    proceedingJoinPoint.getSignature().toShortString(), e.getMessage(), e);
            log.debug("Stack trace : {}", getShortStackTrace(e));

            throw e;
        }
    }

    // Method 정보 추출
    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        return methodSignature.getMethod();
    }

    // Error Stack Trace
    private String getShortStackTrace(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        int limit = Math.min(stackTrace.length, ErrorStackTraceNum);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
