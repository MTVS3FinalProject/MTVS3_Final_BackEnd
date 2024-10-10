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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QRService {

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;
    private static final String QR_FOR_SIGNUP = "";

    public byte[] generateSignUpQR() {

        try {
            // QR Code - BitMatrix: qr 정보 생성
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(QR_FOR_SIGNUP, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

            // QR Code - Image 생성
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
