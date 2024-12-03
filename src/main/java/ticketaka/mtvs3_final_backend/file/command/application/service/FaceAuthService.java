package ticketaka.mtvs3_final_backend.file.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.FaceAuthResponseDTO;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileCommandRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.service.FaceAuthFeignClient;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaceAuthService {

    private final FileCommandService fileCommandService;

    private final MemberRepository memberRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final FileCommandRepository fileCommandRepository;

    private final FaceAuthFeignClient faceAuthFeignClient;
    private final PasswordEncoder passwordEncoder;

    /*
        얼굴 인식
     */
    @Transactional
    public void recognizeMember(FaceAuthRequestDTO.recognizeMemberDTO requestDTO) {

        String imgUrl = fileCommandService.uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        FaceAuthResponseDTO.recognizeFaceDTO responseDTO = faceAuthFeignClient.recognizeFace(new FaceAuthRequestDTO.recognizeFaceDTO(imgUrl));

        if(responseDTO.result() == 0) {
            throw new Exception400(responseDTO.message());
        }

        fileCommandService.setFileUploadForSignUp(requestDTO.email(), requestDTO.secondPwd(), imgUrl);
    }
    
    /*
        얼굴 인증
     */
    @Transactional
    public void verificationMember(FaceAuthRequestDTO.verificationMemberDTO requestDTO) {

        // FileUploadForAuth 확인
        FileUploadForAuth fileUpload = fileCommandService.uploadImgForVerification(requestDTO);

        Long currentMemberId = Long.parseLong(fileUpload.getCode());

        // 유저 확인
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("해당하는 회원을 찾을 수 없습니다."));

        // 2차 비밀번호 확인
        if(!passwordEncoder.matches(requestDTO.secondPwd(), member.getSecondPwd())) {
            throw new Exception401("회원 인증에 실패하였습니다.");
        }

        FaceAuthResponseDTO.identifyFaceDTO responseDTO = getIdentifyFaceDTO(currentMemberId, fileUpload.getImgUrl());

        log.info("{}", responseDTO);

        // 결과 확인
        if (responseDTO.match_result() == 0) {

            fileUpload.setUploadStatus(UploadStatus.FAIL);
            fileCommandService.newFile(RelationType.MEMBER, currentMemberId, fileUpload.getImgUrl(), FilePurpose.VERIFICATION);

            throw new Exception401("얼굴 인식에 실패하였습니다.");
        }

        fileUpload.setUploadStatus(UploadStatus.SUCCESS);

        // File 생성
        fileCommandService.newFile(RelationType.MEMBER, currentMemberId, fileUpload.getImgUrl(), FilePurpose.VERIFICATION);
    }

    /*
        티켓 사용 신원 인증
     */
    public void verifyTicketOwner(Long memberId, FaceAuthRequestDTO.verifyTicketOwnerDTO requestDTO) {

        // Member 조회
        Member member = getMember(memberId);

        // Ticket 조회
        Ticket ticket = getTicket(requestDTO.ticketId());

        // Ticket 소유 여부 확인
        if (!ticket.getMemberId().equals(memberId)) {
            throw new Exception403("해당 티켓의 소유주가 아닙니다.");
        }

        // 2차 비밀번호 확인
        if (!member.getSecondPwd().equals(requestDTO.secondPwd())) {
            throw new Exception400("비밀번호가 틀렸습니다.");
        }

        String imgUrl = fileCommandService.uploadImg(requestDTO.image(), requestDTO.image().getOriginalFilename());

        // 얼굴 인증
        FaceAuthResponseDTO.identifyFaceDTO responseDTO = getIdentifyFaceDTO(memberId, imgUrl);

        log.info("{}", responseDTO);
    }

    // 회원 확인
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("회원 인식이 되지 않습니다."));
    }

    // Ticket 조회
    private Ticket getTicket(Long ticketId) {
        return ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception403("해당 번호의 티켓은 존재하지 않습니다."));
    }

    // 회원 인증 파일 이미지 조회
    private File getOriginImgUrl(Long currentMemberId) {

        return fileCommandRepository.findByMemberForVerification(RelationType.MEMBER, currentMemberId, FilePurpose.SIGNUP)
                .orElseThrow(() -> new Exception401("해당 회원에게는 인증용 사진이 없습니다."));
    }

    // 회원 인증
    private FaceAuthResponseDTO.identifyFaceDTO getIdentifyFaceDTO(Long currentMemberId, String fileUrl) {

        // 유저 이미지 파일 조회
        File currentMemberImgFile = getOriginImgUrl(currentMemberId);

        // FeignRequestDTO 생성
        FaceAuthRequestDTO.identifyFaceDTO feignRequestDTO = new FaceAuthRequestDTO.identifyFaceDTO(
                currentMemberImgFile.getFileUrl(),
                fileUrl
        );

        // AI 통신
        return faceAuthFeignClient.identifyFace(feignRequestDTO);
    }
}
