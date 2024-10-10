package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.file.command.domain.service.FaceAuthFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaceAuthService {

    private final FaceAuthFeignClient faceAuthFeignClient;
}
