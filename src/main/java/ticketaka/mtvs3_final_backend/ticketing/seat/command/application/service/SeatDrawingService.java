package ticketaka.mtvs3_final_backend.ticketing.seat.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatDrawingService {

    private final MemberRepository memberRepository;

    /*
        추첨 시작 알림
     */
    public List<String> drawingNotification(Long concertId, Long seatId) {
        return getMembersForDrawing(concertId, seatId).stream()
                .map(member -> member.getMemberInfo().getNickname())
                .toList();
    }

    // 해당 Concert & Seat 에 접수한 회원 목록 조회
    private List<Member> getMembersForDrawing(Long concertId, Long seatId) {
        return memberRepository.findByConcertIdAndSeatIdAndMemberSeatStatus(
                concertId, seatId, MemberSeatStatus.RECEIVED
        );
    }
}
