package ticketaka.mtvs3_final_backend.seat.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;

import java.util.List;

@Repository
public interface MemberSeatRepository extends JpaRepository<MemberSeat, Long> {

    Long countBySeatIdAndMemberSeatStatus(Long seatId, MemberSeatStatus status);

    List<MemberSeat> findByMemberIdAndConcertIdAndMemberSeatStatus(Long memberId, Long concertId, MemberSeatStatus memberSeatStatus);
}
