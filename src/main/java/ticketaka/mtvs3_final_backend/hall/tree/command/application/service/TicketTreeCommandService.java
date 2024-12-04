package ticketaka.mtvs3_final_backend.hall.tree.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception403;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.query.repository.FileQueryRepository;
import ticketaka.mtvs3_final_backend.hall.tree.command.application.dto.TicketTreeCommandResponseDTO;
import ticketaka.mtvs3_final_backend.hall.tree.command.domain.model.TicketTree;
import ticketaka.mtvs3_final_backend.hall.tree.command.domain.repository.TicketTreeCommandRepository;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.domain.model.CustomTicket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.repository.TicketCustomQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TicketTreeCommandService {

    private final TicketTreeCommandRepository ticketTreeCommandRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;
    private final TicketCustomQueryRepository ticketCustomQueryRepository;
    private final FileQueryRepository fileQueryRepository;

    /*
        타캣 나무 등록
     */
    public TicketTreeCommandResponseDTO.registerTicketTreeDTO registerTicketTree(Long memberId, Long ticketId) {

        getMember(memberId);
        getTicket(ticketId);

        CustomTicket customTicket = ticketCustomQueryRepository.findByTicketIdOrderByCreatedAtDesc(ticketId)
                .orElseThrow(() -> new Exception403("커스텀한 티켓 이미지만 등록하실 수 있습니다."));
        File file = fileQueryRepository.findByRelationTypeAndRelationId(RelationType.CUSTOM_TICKET, customTicket.getId())
                .orElseThrow(() -> new Exception400("커스텀한 티켓 이미지를 찾을 수 없습니다."));

        TicketTree ticketTree = newTicketTree(memberId, file.getId());

        return new TicketTreeCommandResponseDTO.registerTicketTreeDTO(
                ticketTree.getId().intValue(),
                file.getFileUrl()
        );
    }

    // 회원 확인
    private void getMember(Long memberId) {
        memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("회원 인식이 되지 않습니다."));
    }

    // Ticket 조회
    private void getTicket(Long ticketId) {
        ticketQueryRepository.findById(ticketId)
                .orElseThrow(() -> new Exception400("해당 티켓을 조회할 수 없습니다."));
    }

    // TicketTree 생성
    private TicketTree newTicketTree(Long memberId, Long fileId) {
        TicketTree ticketTree = TicketTree.builder()
                .memberId(memberId)
                .fileId(fileId)
                .build();
        return ticketTreeCommandRepository.save(ticketTree);
    }
}
