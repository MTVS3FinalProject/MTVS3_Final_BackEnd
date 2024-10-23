package ticketaka.mtvs3_final_backend.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.MemberSeatStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m FROM Member m " +
            "JOIN MemberSeat ms ON m.id = ms.id " +
            "WHERE ms.concertId = :concertId " +
            "AND ms.seatId = :seatId " +
            "AND ms.memberSeatStatus = :status")
    List<Member> findByConcertIdAndSeatId(@Param("concertId") Long concertId,
                                          @Param("seatId") Long seatId,
                                          @Param("status") MemberSeatStatus status);
}
