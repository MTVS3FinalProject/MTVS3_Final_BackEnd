package ticketaka.mtvs3_final_backend.admin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminCommandRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.TitleAdminCommandRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TitleAdminCommandService {

    private final TitleAdminCommandRepository titleAdminCommandRepository;

    /*
            Title 추가
         */
    public Title saveTitle(AdminCommandRequestDTO.uploadTitleDTO requestDTO) {
        return titleAdminCommandRepository.save(newTitle(requestDTO));
    }

    // Title 생성
    private Title newTitle(AdminCommandRequestDTO.uploadTitleDTO requestDTO) {
        return Title.builder()
                .titleName(requestDTO.titleName())
                .titleScript(requestDTO.titleScript())
                .titleRarity(TitleRarity.fromString(requestDTO.titleRarity()))
                .build();
    }
}
