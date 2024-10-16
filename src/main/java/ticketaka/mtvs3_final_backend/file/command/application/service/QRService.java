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
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUpload;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForSignUp;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForSignUpRedisRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadRedisRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QRService {

    private final MemberRepository memberRepository;
    private final FileUploadRedisRepository fileUploadRedisRepository;
    private final FileUploadForSignUpRedisRepository fileUploadForSignUpRedisRepository;

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;
    private static final String QR_FORMAT = "PNG";
    private static final String QR_FOR_SIGNUP = "https://125.132.216.190:7979/camera/signup";
    private static final String QR_FOR_VERIFICATION = "https://125.132.216.190:7979/camera/verfication";

    /*
        회원 가입 용 QR 생성
     */
    public byte[] generateSignUpQR(QRRequestDTO.generateQRDTO requestDTO) {

        // 이메일 중복 확인
        memberRepository.findByEmail(requestDTO.email())
                .ifPresent(member -> { throw new Exception400("이미 가입된 이메일입니다."); });

        String targetUrlWithEmail = QR_FOR_SIGNUP + "?email=" + requestDTO.email();

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 가입 용 사진 정보 저장 준비
        saveFileUploadForSignUp(requestDTO.email());

        return outputStream.toByteArray();
    }

    /*
        회원 가입 용 사진 업로드 성공 확인
     */
    public void checkSignUpQR(QRRequestDTO.generateQRDTO requestDTO) {

        FileUploadForSignUp fileUpload = fileUploadForSignUpRedisRepository.findById(requestDTO.email())
                .orElseThrow(() -> new Exception400("사진 인증 대기 상태가 아닙니다."));

        validateFileUpload(fileUpload);
    }

    /*
        회원 인증 용 QR 생성
     */
    public byte[] generateVerificationQR(Long currentMemberId) {

        validateMember(currentMemberId);

        String targetUrlWithEmail = QR_FOR_VERIFICATION + "?id=" + currentMemberId;

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 인증 용 상태 준비
        saveFileUpload(currentMemberId.toString());

        return outputStream.toByteArray();
    }

    /*
        회원 인증 용 사진 업로드 성공 확인
     */
    public void checkVerificationQR(Long currentMemberId) {

        validateMember(currentMemberId);

        FileUpload fileUpload = fileUploadRedisRepository.findById(currentMemberId)
                .orElseThrow(() -> new Exception400("사진 인증 대기 상태가 아닙니다."));

        validateFileUpload(fileUpload);
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

    // FileUploadForSignUp 생성
    private void saveFileUploadForSignUp(String email) {

        FileUploadForSignUp fileUpload = FileUploadForSignUp.builder()
                .id(email)
                .uploadStatus(UploadStatus.PENDING)
                .build();

        fileUploadRedisRepository.save(fileUpload);
    }

    // FileUpload 생성
    private void saveFileUpload(String Id) {

        FileUpload fileUpload = FileUpload.builder()
                .id(Id)
                .uploadStatus(UploadStatus.PENDING)
                .build();

        fileUploadRedisRepository.save(fileUpload);
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
}
