package ticketaka.mtvs3_final_backend.file.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
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

    // File Image 조회
    public String getFileImage(RelationType relationType, Long id) {

        File file = fileQueryRepository.findByRelationTypeAndRelationId(relationType, id)
                .orElseThrow(() -> new Exception400("해당 Ticket 이미지를 찾을 수 없습니다."));
        return file.getFileUrl();
    }

    public String getQRImage(RelationType relationType, Long ticketId, FilePurpose filePurpose) {

        File file = fileQueryRepository.findByRelationTypeAndRelationIdAndFilePurpose(relationType, ticketId, filePurpose)
                .orElseThrow(() -> new Exception400("해당 Ticket QR 이미지를 찾을 수 없습니다."));
        return file.getFileUrl();
    }
}
