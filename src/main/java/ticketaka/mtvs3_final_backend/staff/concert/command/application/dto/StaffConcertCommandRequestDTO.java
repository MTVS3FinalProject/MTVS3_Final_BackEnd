package ticketaka.mtvs3_final_backend.staff.concert.command.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StaffConcertCommandRequestDTO {

    /*
        공연장 생성
     */
    public record createConcertDTO(
            String concertName,
            LocalDateTime concertDate,
            Integer ageRestriction,
            Integer receptionLimit,
            List<SeatSectionDTO> seatSectionList
    ) {
    }

    // 구역 정보
    public record SeatSectionDTO(
            String section,
            Integer floor,
            Integer price,
            LocalDateTime drawingTime
    ) {
    }
}
