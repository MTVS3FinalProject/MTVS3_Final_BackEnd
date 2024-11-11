package ticketaka.mtvs3_final_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.ConcertStatus;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.repository.ConcertRepository;
import ticketaka.mtvs3_final_backend.file.command.domain.model.File;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.file.command.domain.repository.FileCommandRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.Seat;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.model.SeatStatus;
import ticketaka.mtvs3_final_backend.ticketing.seat.command.domain.repository.SeatCommandRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleRarity;
import ticketaka.mtvs3_final_backend.title.command.domain.model.TitleType;
import ticketaka.mtvs3_final_backend.title.command.domain.repository.TitleCommandRepository;

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
                                       FileCommandRepository fileCommandRepository,
                                       PasswordEncoder passwordEncoder,
                                       ConcertRepository concertRepository,
                                       SeatCommandRepository seatCommandRepository,
                                       TitleCommandRepository titleCommandRepository) {
        return args -> {
            
            // Member 저장
            Member member1 = newMember("Dorian", "test@test.com", "test1234", "1234", LocalDate.of(1996, 3, 15), 0, passwordEncoder);
            Member member2 = newMember("INUK", "inuk@test.com", "test1234", "1234", LocalDate.of(1998, 9, 5), 0, passwordEncoder);
            Member member3 = newMember("kjm", "wjdals4433@naver.com", "test1234", "1234", LocalDate.of(1998, 3, 19), 0, passwordEncoder);
            Member member4 = newMember("lee", "lee@test.com", "test1234", "1234", LocalDate.of(1996, 10, 12), 0, passwordEncoder);
            Member member5 = newMember("guswns", "whgdk0513@gmail.com", "test1234", "1234", LocalDate.of(1997, 5, 13), 0, passwordEncoder);
            Member member6 = newMember("g0r0kke", "g0r0kke@test.com", "test1234", "1234", LocalDate.of(2003, 2, 10), 0, passwordEncoder);
            Member member7 = newMember("0314", "sdco3062@naver.com", "test1234", "1234", LocalDate.of(2000, 11, 6), 0, passwordEncoder);
            Member member8 = newMember("슈가룬", "may@naver.com", "test1234", "1234", LocalDate.of(1993, 4, 21), 0, passwordEncoder);
            member1.setCoin(100000);
            member2.setCoin(100000);
            member3.setCoin(100000);
            member4.setCoin(100000);
            member5.setCoin(100000);
            member6.setCoin(100000);
            member7.setCoin(100000);
            member8.setCoin(100000);
            memberRepository.saveAll(Arrays.asList(
                    member1, member2, member3, member4, member5, member6, member7, member8
            ));
            fileCommandRepository.saveAll(Arrays.asList(
                    newFile(RelationType.MEMBER, 3L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024163127.png?generation=1729755087790928&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 4L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024181004.png?generation=1729761005300338&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 5L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241024163356.png?generation=1729755237994884&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 6L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/captured-photo-20241025124448.png?generation=1729827892211852&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 7L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/signup-20241028175000.png?generation=1730105402094670&alt=media", FilePurpose.SIGNUP),
                    newFile(RelationType.MEMBER, 8L, "https://storage.googleapis.com/download/storage/v1/b/mtvs3-final-storage.appspot.com/o/signup-20241028175534.png?generation=1730105736125941&alt=media", FilePurpose.SIGNUP)
                    ));
            
            // Concert 저장
            Concert concert01 = newConcert("NewJeans Bunnies Camp", LocalDateTime.of(2024, 11, 1, 19, 0), 18, 2);
            concert01.setConcertStatus(ConcertStatus.RESERVING);
            concertRepository.saveAll(Arrays.asList(
                    concert01
            ));
            
            // Seat 저장
            seatCommandRepository.saveAll(Arrays.asList(
                    newSeat(2, "A1", "13", 19999, LocalDateTime.of(2024, 10, 22, 17, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A1", "15", 29999, LocalDateTime.of(2024, 10, 22, 17, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A1", "17", 19999, LocalDateTime.of(2024, 10, 22, 18, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A2", "19", 14999, LocalDateTime.of(2024, 10, 22, 18, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A2", "21", 24999, LocalDateTime.of(2024, 10, 22, 18, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A2", "23", 19999, LocalDateTime.of(2024, 10, 22, 18, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "A2", "25", 29999, LocalDateTime.of(2024, 10, 22, 19, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B1", "27", 15999, LocalDateTime.of(2024, 10, 22, 19, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B1", "29", 21999, LocalDateTime.of(2024, 10, 22, 19, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B1", "31", 17999, LocalDateTime.of(2024, 10, 22, 19, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B1", "33", 19999, LocalDateTime.of(2024, 10, 22, 20, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B2", "35", 29999, LocalDateTime.of(2024, 10, 22, 20, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B2", "37", 14999, LocalDateTime.of(2024, 10, 22, 20, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B2", "39", 24999, LocalDateTime.of(2024, 10, 22, 20, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "B2", "41", 17999, LocalDateTime.of(2024, 10, 22, 21, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C1", "43", 19999, LocalDateTime.of(2024, 10, 22, 21, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C1", "45", 29999, LocalDateTime.of(2024, 10, 22, 21, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C1", "47", 15999, LocalDateTime.of(2024, 10, 22, 21, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C1", "49", 14999, LocalDateTime.of(2024, 10, 22, 22, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C2", "51", 24999, LocalDateTime.of(2024, 10, 22, 22, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C2", "53", 17999, LocalDateTime.of(2024, 10, 22, 22, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C2", "55", 19999, LocalDateTime.of(2024, 10, 22, 22, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "C2", "57", 29999, LocalDateTime.of(2024, 10, 22, 23, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D1", "59", 14999, LocalDateTime.of(2024, 10, 22, 23, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D1", "61", 15999, LocalDateTime.of(2024, 10, 22, 23, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D1", "63", 21999, LocalDateTime.of(2024, 10, 22, 23, 45), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D1", "65", 17999, LocalDateTime.of(2024, 10, 23, 00, 00), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D2", "67", 24999, LocalDateTime.of(2024, 10, 23, 00, 15), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D2", "69", 29999, LocalDateTime.of(2024, 10, 23, 00, 30), concert01, SeatStatus.AVAILABLE),
                    newSeat(2, "D2", "71", 19999, LocalDateTime.of(2024, 10, 23, 00, 45), concert01, SeatStatus.AVAILABLE)
            ));

            // Title 저장
            titleCommandRepository.saveAll(Arrays.asList(
                    newTitle("Concert", concert01.getId(), "토끼단의 리더", "뉴진스 팬덤 버니즈의 리더로 인정받은 팬", "Unique"),
                    newTitle("Concert", concert01.getId(), "하입보이 지니", "뉴진스 곡 'Hype Boy'의 열렬한 팬", "Unique"),
                    newTitle("Concert", concert01.getId(), "디토의 수호자", "Ditto'를 지키는 뉴진스 팬", "Unique"),
                    newTitle("Concert", concert01.getId(), "Cookie Run의 Master", "뉴진스 곡 'Cookie'를 사랑하는 팬", "Unique"),
                    newTitle("Concert", concert01.getId(), "OMG의 전설", "OMG' 곡에 완벽히 매료된 팬", "Unique"),
                    newTitle("Concert", concert01.getId(), "Super Shy Champion", "뉴진스 곡 'Super Shy'를 완벽히 아는 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "ETA의 시간여행자", "ETA'를 통해 뉴진스와 시간 여행을 즐기는 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "뉴진스의 큐레이터", "뉴진스의 모든 곡을 큐레이팅한 진정한 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "버니즈의 수호천사", "버니즈 팬덤을 지키는 수호천사", "Rare"),
                    newTitle("Concert", concert01.getId(), "Get Up 얼리버드", "뉴진스 콘서트에 가장 일찍 참여한 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "뉴진스의 포토그래퍼", "뉴진스의 모든 순간을 포착하는 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "Cool With You 마스터", "뉴진스의 'Cool With You'에 완전히 빠진 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "New Jeans 창단멤버", "뉴진스 팬덤의 초기 멤버로 활동 중인 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "Attention 셀러브리티", "Attention' 곡에 대한 특별한 애정을 가진 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "Zero to Hero", "뉴진스 팬덤의 성장과 함께하는 팬", "Rare"),
                    newTitle("Concert", concert01.getId(), "뉴진스의 뮤즈", "뉴진스의 영감을 불어넣는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "ASAP 스피드스터", "뉴진스와 함께 신속하게 참여하는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "뉴진스 패션 아이콘", "뉴진스 스타일을 대표하는 패셔니스타", "Common"),
                    newTitle("Concert", concert01.getId(), "글로벌 버니즈", "글로벌 팬덤 버니즈의 일원", "Common"),
                    newTitle("Concert", concert01.getId(), "뉴진스 트렌드세터", "뉴진스 트렌드와 함께하는 진정한 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "Billboard Hot 100", "뉴진스의 빌보드 진입을 자랑스러워하는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "뉴진스 아카데미", "뉴진스의 모든 곡을 공부한 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "버니즈 메이커", "뉴진스 팬덤 버니즈의 창조자", "Common"),
                    newTitle("Concert", concert01.getId(), "뉴진스 앰버서더", "뉴진스를 전 세계에 알리는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "Dance Breaker", "뉴진스 콘서트에서 춤을 즐기는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "버니즈 스토리텔러", "뉴진스의 이야기를 전하는 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "뉴진스 히스토리안", "뉴진스의 역사를 아는 진정한 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "음원강자", "뉴진스의 모든 음원을 섭렵한 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "버니즈 아티스트", "뉴진스 팬 아트를 즐기는 아티스트 팬", "Common"),
                    newTitle("Concert", concert01.getId(), "레전드 버니즈", "뉴진스 팬덤 내에서 전설이 된 팬", "Common")
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

    private Concert newConcert(String name, LocalDateTime concertDate, int ageRestriction, int receptionLimit) {
        return Concert.builder()
                .name(name)
                .concertDate(concertDate)
                .ageRestriction(ageRestriction)
                .receptionLimit(receptionLimit)
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

    private Title newTitle(String titleType, Long concertId, String titleName, String titleScript, String titleRarity) {
        return Title.builder()
                .titleType(TitleType.fromString(titleType))
                .concertId(concertId)
                .titleName(titleName)
                .titleScript(titleScript)
                .titleRarity(TitleRarity.fromString(titleRarity))
                .build();
    }
}
