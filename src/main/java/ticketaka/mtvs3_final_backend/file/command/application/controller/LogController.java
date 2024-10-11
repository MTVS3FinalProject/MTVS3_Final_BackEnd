package ticketaka.mtvs3_final_backend.file.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/log")
public class LogController {

    @PostMapping
    public ResponseEntity<?> logError(@RequestBody String errorLog) {

        log.info("Error Log : {}", errorLog);  // 로그에 에러 메시지 출력
        System.out.println("message = " + errorLog);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
