package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.service.FaceAuthFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaceAuthService {

    private final FaceAuthFeignClient faceAuthFeignClient;

    /*
        얼굴 인식
     */
    public void identifyFace(FaceAuthRequestDTO.getCurrentFaceImgDTO requestDTO) {

        // 유저 이미지 조회
        String originImgUrl = "";

        // FeignRequestDTO 생성
        FaceAuthRequestDTO.identifyFaceDTO feignRequestDTO = new FaceAuthRequestDTO.identifyFaceDTO(
                originImgUrl,
                requestDTO.imgUrl()
        );

        // AI 통신
        FaceAuthResponseDTO.identifyFaceDTO responseDTO = faceAuthFeignClient.identifyFace(feignRequestDTO);

        log.info("{}", responseDTO);

        // 결과 확인
        if (responseDTO.match_result() == 0) {
            throw new Exception401("얼굴 인식에 실패하였습니다.");
        }
    }
}
