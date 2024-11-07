package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.service.FaceAuthFeignClient;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaceAuthService {

    private final FileService fileService;

    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final FaceAuthFeignClient faceAuthFeignClient;
    private final PasswordEncoder passwordEncoder;

    /*
        얼굴 인식
     */
    public void recognizeMember(FaceAuthRequestDTO.recognizeMemberDTO requestDTO) {

        String imgUrl = fileService.uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        FaceAuthResponseDTO.recognizeFaceDTO responseDTO = faceAuthFeignClient.recognizeFace(new FaceAuthRequestDTO.recognizeFaceDTO(imgUrl));

        if(responseDTO.result() == 0) {
            throw new Exception400(responseDTO.message());
        }

        fileService.setFileUploadForSignUp(requestDTO.email(), requestDTO.secondPwd(), imgUrl);
    }
    
    /*
        얼굴 인증
     */
    public void verificationMember(FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        // FileUploadForAuth 확인
        FileUploadForAuth fileUpload = fileService.uploadImgForVerification(requestDTO);

        Long currentMemberId = Long.parseLong(fileUpload.getCode());

        // 유저 확인
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당하는 회원을 찾을 수 없습니다."));

        // 2차 비밀번호 확인
        if(!passwordEncoder.matches(requestDTO.secondPwd(), member.getSecondPwd())) {
            throw new Exception401("회원 인증에 실패하였습니다.");
        }

        // 유저 이미지 파일 조회
        File currentMemberImgFile = getOriginImgUrl(currentMemberId);

        // FeignRequestDTO 생성
        FaceAuthRequestDTO.identifyFaceDTO feignRequestDTO = new FaceAuthRequestDTO.identifyFaceDTO(
                currentMemberImgFile.getFileUrl(),
                fileUpload.getImgUrl()
        );

        // AI 통신
        FaceAuthResponseDTO.identifyFaceDTO responseDTO = faceAuthFeignClient.identifyFace(feignRequestDTO);

        log.info("{}", responseDTO);

        // 결과 확인
        if (responseDTO.match_result() == 0) {

            fileUpload.setUploadStatus(UploadStatus.FAIL);
            fileService.newFile(RelationType.MEMBER, currentMemberId, fileUpload.getImgUrl(), FilePurpose.VERIFICATION);

            throw new Exception401("얼굴 인식에 실패하였습니다.");
        }

        fileUpload.setUploadStatus(UploadStatus.SUCCESS);

        // File 생성
        fileService.newFile(RelationType.MEMBER, currentMemberId, fileUpload.getImgUrl(), FilePurpose.VERIFICATION);
    }

    // 회원 인증 파일 이미지 조회
    private File getOriginImgUrl(Long currentMemberId) {

        return fileRepository.findByMemberForVerification(RelationType.MEMBER, currentMemberId, FilePurpose.SIGNUP)
                .orElseThrow(() -> new Exception401("해당 회원에게는 인증용 사진이 없습니다."));
    }
}
