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
import ticketaka.mtvs3_final_backend.file.command.application.dto.QRRequestDTO;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.identification.domain.FileUpload;
import ticketaka.mtvs3_final_backend.redis.identification.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.identification.repository.IdentificationRedisRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QRService {

    private final MemberRepository memberRepository;
    private final IdentificationRedisRepository identificationRedisRepository;

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;
    private static final String QR_FORMAT = "PNG";
    private static final String QR_FOR_SIGNUP = "https://192.168.0.29:5173/camera";
    private static final String QR_FOR_VERIFICATION = "";

    /*
        회원 가입 용 QR 생성
     */
    public byte[] generateSignUpQR(QRRequestDTO.generateQRDTO requestDTO) {

        // 이메일 중복 확인
        memberRepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new Exception400("이미 가입된 이메일입니다."));

        String targetUrlWithEmail = QR_FOR_SIGNUP + "?email=" + requestDTO.email();

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 가입 용 사진 정보 저장 준비
        saveFileUpload(requestDTO.email());

        return outputStream.toByteArray();
    }

    /*
        회원 인증 용 QR 생성
     */
    public byte[] generateVerificationQR(Long currentMemberId) {

        String targetUrlWithEmail = QR_FOR_VERIFICATION + "?id=" + currentMemberId;

        ByteArrayOutputStream outputStream = getByteArrayOutputStream(targetUrlWithEmail);

        // 회원 인증 용 상태 준비
        saveFileUpload(currentMemberId.toString());

        return outputStream.toByteArray();
    }

    // FileUpload 생성
    private void saveFileUpload(String Id) {

        FileUpload fileUpload = FileUpload.builder()
                .id(Id)
                .uploadStatus(UploadStatus.COMPLETED)
                .build();

        identificationRedisRepository.save(fileUpload);
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
}
