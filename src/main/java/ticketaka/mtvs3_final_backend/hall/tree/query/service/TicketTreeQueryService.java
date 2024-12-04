package ticketaka.mtvs3_final_backend.hall.tree.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.repository.FileQueryRepository;
import ticketaka.mtvs3_final_backend.hall.tree.command.domain.model.TicketTree;
import ticketaka.mtvs3_final_backend.hall.tree.query.dto.TicketTreeQueryRequestDTO;
import ticketaka.mtvs3_final_backend.hall.tree.query.repository.TicketTreeQueryRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketTreeQueryService {

    private final TicketTreeQueryRepository ticketTreeQueryRepository;
    private final FileQueryRepository fileQueryRepository;

    /*
        티켓 트리 조회
     */
    public TicketTreeQueryRequestDTO.getTicketTreeDTO getTicketTree() {

        List<TicketTree> ticketTreeList = ticketTreeQueryRepository.findAll();
        List<TicketTreeQueryRequestDTO.ticketTreeDTO> ticketTreeDTOList = ticketTreeList.stream()
                .map(ticketTree -> fileQueryRepository.findById(ticketTree.getFileId())
                        .map(file -> new TicketTreeQueryRequestDTO.ticketTreeDTO(
                                ticketTree.getId().intValue(),
                                file.getFileUrl()
                        ))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return new TicketTreeQueryRequestDTO.getTicketTreeDTO(ticketTreeDTOList);
    }
}
