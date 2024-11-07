package ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.query.repository.MemberQueryRepository;
import ticketaka.mtvs3_final_backend.ticketing.ticket.custom.command.application.dto.TicketCustomCommandResponseDTO;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TicketCustomCommandService {

    private final MemberQueryRepository memberQueryRepository;

    /*
        AI 배경 생성
     */
    public TicketCustomCommandResponseDTO.createAIStickerDTO generateAISticker(Long memberId) {

        // Member 조회
        getMember(memberId);

        // 일일 제한 횟수 확인

        // 생성

        // 반환

        return null;
    }

    // Member 조회
    private Member getMember(Long memberId) {
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new Exception401("해당 회원을 찾을 수 없습니다."));
    }
}
