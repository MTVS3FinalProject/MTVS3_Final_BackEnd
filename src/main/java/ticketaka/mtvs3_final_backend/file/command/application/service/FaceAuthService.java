package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.service.FaceAuthFeignClient;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaceAuthService {

    private final FileRepository fileRepository;
    private final FaceAuthFeignClient faceAuthFeignClient;

    /*
        얼굴 인식
     */
    public void identifyFace(MultipartFile image, String id, Long currentMemberId) {

        // 유저 이미지 파일 조회
        File currentMemberImgFile = getOriginImgUrl(currentMemberId);

        // 인증 이미지 저장
        String faceImgUrl = "";

        // FeignRequestDTO 생성
        FaceAuthRequestDTO.identifyFaceDTO feignRequestDTO = new FaceAuthRequestDTO.identifyFaceDTO(
                currentMemberImgFile.getFileUrl(),
                faceImgUrl
        );

        // AI 통신
        FaceAuthResponseDTO.identifyFaceDTO responseDTO = faceAuthFeignClient.identifyFace(feignRequestDTO);

        log.info("{}", responseDTO);

        // 결과 확인
        if (responseDTO.match_result() == 0) {
            throw new Exception401("얼굴 인식에 실패하였습니다.");
        }
    }

    // 회원 인증 파일 이미지 조회
    private File getOriginImgUrl(Long currentMemberId) {

        return fileRepository.findByMemberForVerification(RelationType.MEMBER, currentMemberId, FilePurpose.SIGNUP)
                .orElseThrow(() -> new Exception401("해당 회원에게는 인증용 사진이 없습니다."));
    }
}
