package ticketaka.mtvs3_final_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import ticketaka.mtvs3_final_backend.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.seat.command.domain.repository.SeatRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class Mtvs3FinalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mtvs3FinalBackendApplication.class, args);
    }

    @Profile("local")
    @Bean
    CommandLineRunner localServerStart(MemberRepository memberRepository,
                                       FileRepository fileRepository,
                                       PasswordEncoder passwordEncoder,
                                       ConcertRepository concertRepository,
                                       SeatRepository seatRepository) {
        return args -> {
            Member member1 = newMember("Dorian", "test@test.com", "test1234", "1234", LocalDate.of(1996, 3, 15), 0, passwordEncoder);
            Member member2 = newMember("INUK", "inuk@test.com", "test1234", "2469", LocalDate.of(1998, 9, 5), 0, passwordEncoder);
            Member member3 = newMember("kjm", "wjdals4433@naver.com", "test1234", "1234", LocalDate.of(1998, 3, 19), 0, passwordEncoder);
            Member member4 = newMember("lee", "lee@test.com", "test1234", "1234", LocalDate.of(1996, 10, 12), 0, passwordEncoder);
            Member member5 = newMember("guswns", "whgdk0513@gmail.com", "test1234", "1234", LocalDate.of(1997, 5, 13), 0, passwordEncoder);
            member1.setCoin(100000);
            member2.setCoin(100000);
            member3.setCoin(100000);
            member4.setCoin(100000);
            member5.setCoin(100000);
            memberRepository.saveAll(Arrays.asList(
                    member1, member2, member3, member4, member5
            ));
            fileRepository.saveAll(Arrays.asList(
                    newFile(RelationType.MEMBER, 3L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024163127.png?generation=1729755087790928&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 4L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024181004.png?generation=1729761005300338&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 5L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024163356.png?generation=1729755237994884&alt=media", FilePurpose.SIGNUP)
                    ));
            Concert concert01 = newConcert("Concert01", 2, LocalDateTime.of(2024, 11, 1, 19, 0));
            concertRepository.saveAll(Arrays.asList(
                    concert01
            ));
            seatRepository.saveAll(Arrays.asList(
                    newSeat(2, "A1", "13", 19999, LocalDateTime.of(2024, 10, 22, 17, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A1", "15", 29999, LocalDateTime.of(2024, 10, 22, 17, 45), concert01, SeatStatus.UNAVAILABLE)
            ));
        };
    }

    private Member newMember(String nickname, String email, String password, String secondPwd, LocalDate birth, int authority, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .secondPwd(passwordEncoder.encode(secondPwd))
                .birth(birth)
                .authority(Authority.fromInt(authority))
                .status(Status.ACTIVE)
                .build();
    }

    private File newFile(RelationType relationType, Long relationId, String fileUrl, FilePurpose filePurpose) {
        return File.builder()
                .relationType(relationType)
                .relationId(relationId)
                .fileUrl(fileUrl)
                .filePurpose(filePurpose)
                .build();
    }

    private Concert newConcert(String name, int receptionLimit, LocalDateTime concertDate) {
        return Concert.builder()
                .name(name)
                .receptionLimit(receptionLimit)
                .concertDate(concertDate)
                .build();
    }

    private Seat newSeat(int floor, String section, String number, int price, LocalDateTime drawingTime, Concert concert, SeatStatus seatStatus) {
        return Seat.builder()
                .floor(floor)
                .section(section)
                .number(number)
                .price(price)
                .drawingTime(drawingTime)
                .concert(concert)
                .seatStatus(seatStatus)
                .build();
    }
}
