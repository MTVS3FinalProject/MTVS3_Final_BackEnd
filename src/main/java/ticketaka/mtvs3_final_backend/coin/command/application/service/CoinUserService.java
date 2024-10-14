package ticketaka.mtvs3_final_backend.coin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinAcquisition;
import ticketaka.mtvs3_final_backend.coin.command.domain.model.CoinHistory;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CoinUserService {

    private final MemberRepository memberRepository;
    private final CoinAcquisitionRepository coinAcquisitionRepository;
    private final CoinHistoryRepository coinHistoryRepository;
}