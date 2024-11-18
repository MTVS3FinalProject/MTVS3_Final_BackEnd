package ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;

import java.util.List;

@Repository
public interface TicketQueryRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByMemberId(Long memberId);

    @Query("SELECT new ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO.getMemberTicketDTO(" +
            "t.id, c.name, CONCAT(s.section, '구역 ', s.number, '번')," +
            "CASE WHEN ct.id IS NOT NULL " +
            "   THEN ctf.fileUrl " +
            "   ELSE tf.fileUrl " +
            "END ) " +
            "FROM Ticket t JOIN Concert c ON t.concertId = c.id " +
            "JOIN Seat s ON t.seatId = s.id " +
            "LEFT JOIN CustomTicket ct ON t.id = ct.ticketId " +
            "LEFT JOIN File ctf ON ctf.relationId = ct.id AND ctf.relationType = 'CUSTOM_TICKET' " +
            "LEFT JOIN File tf ON tf.relationId = c.id AND tf.relationType = 'CONCERT' " +
            "WHERE t.memberId = :memberId")
    List<MemberQueryResponseDTO.getMemberTicketDTO> findAllByMemberIdAndRelationType(Long memberId);
}
