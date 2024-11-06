package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.title.query.service.TitleQueryService;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberInventoryService {

    private final TitleQueryService titleQueryService;
    private final TicketQueryService ticketQueryService;
    private final StickerQueryService stickerQueryService;
}
