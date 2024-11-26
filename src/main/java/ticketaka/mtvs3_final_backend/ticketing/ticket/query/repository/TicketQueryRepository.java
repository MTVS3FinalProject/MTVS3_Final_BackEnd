package ticketaka.mtvs3_final_backend.ticketing.ticket.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.query.dto.MemberQueryResponseDTO;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTicketDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.command.domain.model.Ticket;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDTO;
import ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDetailDTO;

import java.util.List;

@Repository
public interface TicketQueryRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT new ticketaka.mtvs3_final_backend.member.query.dto.getMemberTicketDTO(" +
            "t.id, c.name, CONCAT(s.section, '구역 ', s.number, '번')," +
            "CASE WHEN ct.id IS NOT NULL " +
            "   THEN ctf.fileUrl " +
            "   ELSE tf.fileUrl " +
            "END ) " +
            "FROM Ticket t " +
            "JOIN Concert c ON t.concertId = c.id " +
            "JOIN Seat s ON t.seatId = s.id " +
            "LEFT JOIN CustomTicket ct ON t.id = ct.ticketId AND ct.createdAt = (" +
            "   SELECT MAX(subCt.createdAt) FROM CustomTicket subCt WHERE subCt.ticketId = t.id" +
            ") " +
            "LEFT JOIN File ctf ON ctf.relationId = ct.id AND ctf.relationType = 'CUSTOM_TICKET' " +
            "LEFT JOIN File tf ON tf.relationId = c.id AND tf.relationType = 'CONCERT' AND tf.filePurpose = 'TICKET' " +
            "WHERE t.memberId = :memberId")
    List<getMemberTicketDTO> findAllByMemberIdAndRelationType(@Param("memberId") Long memberId);


    @Query("SELECT new ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDTO(" +
            "t.id, c.name, c.concertDate, " +
            "CONCAT(s.section, '구역 ', s.number, '번')," +
            "CASE WHEN ct.id IS NOT NULL " +
            "   THEN ctf.fileUrl " +
            "   ELSE tf.fileUrl " +
            "END, " +
            "qf.fileUrl) " +
            "FROM Ticket t JOIN Concert c ON t.concertId = c.id " +
            "JOIN Seat s ON t.seatId = s.id " +
            "LEFT JOIN CustomTicket ct ON t.id = ct.ticketId AND ct.createdAt = (" +
            "   SELECT MAX(subCt.createdAt) FROM CustomTicket subCt WHERE subCt.ticketId = t.id" +
            ") " +
            "LEFT JOIN File ctf ON ctf.relationId = ct.id AND ctf.relationType = 'CUSTOM_TICKET' " +
            "LEFT JOIN File tf ON tf.relationId = c.id AND tf.relationType = 'CONCERT' AND tf.filePurpose = 'TICKET' " +
            "LEFT JOIN File qf ON qf.relationId = t.id AND qf.relationType = 'TICKET' AND qf.filePurpose = 'VERIFICATION' " +
            "WHERE t.memberId = :memberId")
    List<getTicketDTO> findCustomizableTicketsByMemberId(@Param("memberId") Long memberId);


    @Query("SELECT new ticketaka.mtvs3_final_backend.ticketing.ticket.query.dto.getTicketDetailDTO(" +
            "t.id, c.name, c.concertDate, " +
            "CONCAT(s.section, '구역 ', s.number, '번')," +
            "CASE WHEN ct.id IS NOT NULL " +
            "   THEN ctf.fileUrl " +
            "   ELSE tf.fileUrl " +
            "END, " +
            "bgf.fileUrl, " +
            "qf.fileUrl) " +
            "FROM Ticket t JOIN Concert c ON t.concertId = c.id " +
            "JOIN Seat s ON t.seatId = s.id " +
            "LEFT JOIN CustomTicket ct ON t.id = ct.ticketId AND ct.createdAt = (" +
            "   SELECT MAX(subCt.createdAt) FROM CustomTicket subCt WHERE subCt.ticketId = t.id" +
            ") " +
            "LEFT JOIN File ctf ON ctf.relationId = ct.id AND ctf.relationType = 'CUSTOM_TICKET' " +
            "LEFT JOIN File tf ON tf.relationId = c.id AND tf.relationType = 'CONCERT' AND tf.filePurpose = 'TICKET' " +
            "LEFT JOIN File bgf ON bgf.relationId = c.id AND bgf.relationType = 'CONCERT' AND bgf.filePurpose = 'BACKGROUND' " +
            "LEFT JOIN File qf ON qf.relationId = t.id AND qf.relationType = 'TICKET' AND qf.filePurpose = 'VERIFICATION' " +
            "WHERE t.memberId = :memberId " +
            "AND t.id = :ticketId")
    getTicketDetailDTO findTicketDTOByMemberIdAndTicketId(@Param("memberId") Long memberId, @Param("ticketId") Long ticketId);
}
