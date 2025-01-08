package ticketaka.mtvs3_final_backend.staff.concert.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend.staff.concert.command.application.dto.StaffConcertCommandRequestDTO;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StaffConcertCommandService {

    private final ConcertRepository concertRepository;
    private final SeatCommandRepository seatCommandRepository;

    /*
        공연 생성
     */
    public void createConcert(StaffConcertCommandRequestDTO.createConcertDTO requestDTO) {

        // 공연 생성
        Concert concert = newConcert(
                requestDTO.concertName(),
                requestDTO.concertDate(),
                requestDTO.ageRestriction(),
                requestDTO.receptionLimit()
        );

        List<Seat> seatList = new ArrayList<>();

        for (StaffConcertCommandRequestDTO.SeatSectionDTO seatSectionDTO : requestDTO.seatSectionList()) {
            LocalDateTime drawingTime = seatSectionDTO.drawingTime();
            for (int number = 0; number < seatSectionDTO.seatCount(); number++) {
                Seat seat = Seat.builder()
                        .floor(seatSectionDTO.floor())
                        .section(seatSectionDTO.section())
                        .number(String.valueOf(number + 1))
                        .price(seatSectionDTO.price())
                        .drawingTime(drawingTime)
                        .concert(concert)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build();
                seatList.add(seat);

                drawingTime = drawingTime.plusMinutes(15);
            }
        }

        seatCommandRepository.saveAll(seatList);
    }

    // Concert 생성
    private Concert newConcert(String concertName, LocalDateTime concertDate, Integer ageRestriction, Integer receptionLimit) {
        Concert concert = Concert.builder()
                .name(concertName)
                .concertDate(concertDate)
                .ageRestriction(ageRestriction)
                .receptionLimit(receptionLimit)
                .build();
        return concertRepository.save(concert);
    }
}
