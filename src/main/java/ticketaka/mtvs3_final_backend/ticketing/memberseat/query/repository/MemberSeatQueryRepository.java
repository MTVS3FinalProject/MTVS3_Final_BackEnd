package ticketaka.mtvs3_final_backend.ticketing.memberseat.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeat;
import ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model.MemberSeatStatus;

@Repository
public interface MemberSeatQueryRepository extends JpaRepository<MemberSeat, Long> {

    int countByConcertIdAndSeatIdAndMemberSeatStatus(Long concertId, Long seatId, MemberSeatStatus status);
}
