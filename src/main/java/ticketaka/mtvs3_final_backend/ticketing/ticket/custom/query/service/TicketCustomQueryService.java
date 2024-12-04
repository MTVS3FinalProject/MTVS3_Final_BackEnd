package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.query.dto.TicketCustomQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository.TicketQueryRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCustomQueryService {

    private final TicketQueryRepository ticketQueryRepository;

    public TicketCustomQueryResponseDTO.getCustomTicketListDTO getCustomTicketList(Long memberId) {

        List<TicketCustomQueryResponseDTO.customTicketDTO> customTicketDTOList = ticketQueryRepository.findCustomTicketImage(memberId);

        return new TicketCustomQueryResponseDTO.getCustomTicketListDTO(customTicketDTOList);
    }
}
