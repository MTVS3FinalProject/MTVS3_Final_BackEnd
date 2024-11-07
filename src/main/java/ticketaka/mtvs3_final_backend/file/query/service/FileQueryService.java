package ticketaka.mtvs3_final_backend.file.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.repository.FileQueryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FileQueryService {

    private final FileQueryRepository fileQueryRepository;

    public Map<Long, byte[]> getStickerImgMap(List<Long> stickerIdList) {

        // File 조회
        List<File> stickerFileList = fileQueryRepository.findAllByRelationTypeAndRelationIdIn(RelationType.STICKER, stickerIdList);

        return stickerFileList.stream()
                .collect(Collectors.toMap(
                        File::getRelationId,
                        file -> getImageFromUrl(file.getFileUrl()),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    // ImageUrl 을 통해 byte[] 가져오기 (HTTP 요청 사용)
    protected byte[] getImageFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
