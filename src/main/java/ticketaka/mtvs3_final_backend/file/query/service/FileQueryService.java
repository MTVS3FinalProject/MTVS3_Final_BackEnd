package ticketaka.mtvs3_final_backend.file.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.repository.FileQueryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
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

    public Map<Long, String> getStickerImgMap(List<Long> stickerIdList) {

        // File 조회
        List<File> stickerFileList = fileQueryRepository.findAllByRelationTypeAndRelationIdIn(RelationType.STICKER, stickerIdList);

        return stickerFileList.stream()
                .collect(Collectors.toMap(
                        File::getRelationId,
                        file -> {
                            byte[] imageData = getImageFromUrl(file.getFileUrl());
                            return Base64.getEncoder().encodeToString(imageData);
                        },
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    // File Image 조회
    public String getFileImage(RelationType relationType, Long id) {

        File file = fileQueryRepository.findByRelationTypeAndRelationId(relationType, id)
                .orElseThrow(() -> new Exception400("해당 Ticket 이미지를 찾을 수 없습니다."));

        byte[] imageData = getImageFromUrl(file.getFileUrl());

        return Base64.getEncoder().encodeToString(imageData);
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
