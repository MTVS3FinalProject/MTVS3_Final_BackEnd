package ticketaka.mtvs3_final_backend.file.command.application.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRResponseDTO;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUpload;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForAuthRedisRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadRedisRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QRService {

    private final MemberRepository memberRepository;
    private final FileUploadRedisRepository fileUploadRedisRepository;
    private final FileUploadForAuthRedisRepository fileUploadForAuthRedisRepository;

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;
    private static final String QR_FORMAT = "PNG";
    private static final String QR_FOR_SIGNUP = "https://ticketaka.shop/camera/signup";
    private static final String QR_FOR_VERIFICATION = "https://ticketaka.shop/camera/verification";

    /*
        회원 가입 용 QR 생성
     */
    public byte[] generateSignUpQR(QRRequestDTO.generateSignUpQRDTO requestDTO) {

        // 이메일 중복 확인
        memberRepository.findByEmail(requestDTO.email())
                .ifPresent(member -> { throw new Exception400("이미 가입된 이메일입니다."); });

        String targetUrlWithEmail = QR_FOR_SIGNUP + "?email=" + requestDTO.email();

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 가입 용 사진 정보 저장 준비
        saveFileUploadForAuth(requestDTO.email(), "");

        return outputStream.toByteArray();
    }

    /*
        회원 가입 용 사진 업로드 성공 확인
     */
    public void checkSignUpQR(QRRequestDTO.checkSignUpQRDTO requestDTO) {

        FileUploadForAuth fileUpload = fileUploadForAuthRedisRepository.findById(requestDTO.email())
                .orElseThrow(() -> new Exception400("사진 인증 대기 상태가 아닙니다."));

        validateFileUpload(fileUpload);
    }

    /*
        회원 인증 용 QR 생성
     */
    public QRResponseDTO.generateVerificationQRDTO generateVerificationQR(Long currentMemberId) {

        validateMember(currentMemberId);

        String userCode = createRandomUUID();

        String targetUrlWithEmail = QR_FOR_VERIFICATION + "?userCode=" + userCode;

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 인증 용 상태 준비
        saveFileUploadForAuth(userCode, currentMemberId.toString());

        return new QRResponseDTO.generateVerificationQRDTO(outputStream.toByteArray(), userCode);
    }

    /*
        회원 인증 용 사진 업로드 성공 확인
     */
    public void checkVerificationQR(QRRequestDTO.checkVerificationQRDTO requestDTO, Long currentMemberId) {

        validateMember(currentMemberId);

        FileUploadForAuth fileUpload = fileUploadForAuthRedisRepository.findById(requestDTO.userCode())
                .orElseThrow(() -> new Exception400("사진 인증 대기 상태가 아닙니다."));

        if(!fileUpload.getCode().equals(String.valueOf(currentMemberId))) {
            throw new Exception401("인증 요청 대상과 일치하지 않습니다.");
        }

        validateFileUpload(fileUpload);

        fileUpload.setUploadStatus(UploadStatus.SUCCESS);
        fileUploadRedisRepository.save(fileUpload);
    }

    // QR 생성
    private static ByteArrayOutputStream getByteArrayOutputStream(String targetUrl) {

        try {
            // QR Code - BitMatrix: qr 정보 생성
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(targetUrl, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

            // QR Code - Image 생성
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, QR_FORMAT, outputStream);

            return outputStream;

        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // FileUploadForAuth 생성
    private void saveFileUploadForAuth(String id, String code) {

        FileUploadForAuth fileUpload = FileUploadForAuth.builder()
                .id(id)
                .uploadStatus(UploadStatus.PENDING)
                .code(code)
                .build();

        fileUploadForAuthRedisRepository.save(fileUpload);
    }

    // 파일 유효성 확인
    private static void validateFileUpload(FileUpload fileUpload) {

        if(fileUpload.getUploadStatus().equals(UploadStatus.PENDING)) {
            throw new Exception400("신원 인증 사진을 업로드하지 않았습니다.");
        }

        if(fileUpload.getUploadStatus().equals(UploadStatus.FAIL)) {
            throw new Exception401("신원 인증에 실패하였습니다.");
        }
    }

    // 회원 확인
    private void validateMember(Long currentMemberId) {
        memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception401("회원을 찾을 수 없습니다."));
    }

    // 랜덤 UUID 생성
    private String createRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
