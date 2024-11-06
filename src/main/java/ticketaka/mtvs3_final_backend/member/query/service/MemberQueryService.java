package ticketaka.mtvs3_final_backend.member.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberInventoryService memberInventoryService;

    private final MemberQueryRepository memberQueryRepository;
}
