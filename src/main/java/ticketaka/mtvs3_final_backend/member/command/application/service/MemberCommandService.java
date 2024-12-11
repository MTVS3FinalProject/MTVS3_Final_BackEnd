package ticketaka.mtvs3_final_backend.member.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberCommandResponseDTO;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.member.command.domain.service.COOLSMSService;
import ticketaka.mtvs3_final_backend.redis.ticket.address.domain.TicketAddress;
import ticketaka.mtvs3_final_backend.redis.ticket.address.repository.TicketAddressRedisRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.member.command.domain.repository.MemberTitleCommandRepository;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;
import ticketaka.mtvs3_final_backend.title.query.repository.TitleQueryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final COOLSMSService coolsmsService;

    private final AddressRepository addressRepository;
    private final MemberTitleCommandRepository memberTitleCommandRepository;
    private final MemberTitleQueryRepository memberTitleQueryRepository;

    private final TicketAddressRedisRepository ticketAddressRedisRepository;
    private final TitleQueryRepository titleQueryRepository;

    /*
            티켓 주소지 입력
         */
    public Address saveTicketAddress(Long memberId, Long concertId, Long seatId, Long ticketId) {
        TicketAddress ticketAddress = ticketAddressRedisRepository.findById(TicketAddress.generateTicketAddressId(
                memberId, concertId, seatId)
        ).orElseThrow(() -> new Exception400("주소지 입력이 되지 않았습니다."));

        Address address = newAddress(ticketAddress, memberId, ticketId);
        addressRepository.save(address);

        ticketAddressRedisRepository.delete(ticketAddress);

        return address;
    }

    /*
        메인 타이틀 변경
     */
    @Transactional
    public MemberCommandResponseDTO.changeMainTitleDTO changeMainTitle(Long memberId, Long titleId) {

        // 기존 Main Title 해제
        memberTitleCommandRepository.clearRepresentativeTitle(memberId);

        // Title 조회
        Title title = titleQueryRepository.findById(titleId)
                .orElseThrow(() -> new Exception400("해당 Title 은 존재하지 않습니다."));

        // Main Title 설정
        MemberTitle memberTitle = memberTitleQueryRepository.findByMemberIdAndTitleId(memberId, titleId)
                .orElseThrow(() -> new Exception400("해당 Title 을 소유하고 있지 않습니다."));
        memberTitle.setIsRepresentative(true);
        memberTitleCommandRepository.save(memberTitle);

        return new MemberCommandResponseDTO.changeMainTitleDTO(
                title.getTitleName(),
                title.getTitleRarity().toString()
        );
    }

    /*
        메인 타이틀 제거
     */
    @Transactional
    public void deleteMainTitle(Long currentMemberId) {

        memberTitleCommandRepository.clearRepresentativeTitle(currentMemberId);
    }

    // 티켓 결제 SMS 전송
    public void sendReserveSMS(String phoneNumber, String concertName, String seatInfo, LocalDateTime concertDateTime) {

        String content = generateReserveSMS(concertName, seatInfo, concertDateTime);

//        coolsmsService.sendOne(phoneNumber, content);
    }

    // Address 생성
    private Address newAddress(TicketAddress ticketAddress, Long memberId, Long ticketId) {
        return Address.builder()
                .memberId(memberId)
                .ticketId(ticketId)
                .userName(ticketAddress.getUserName())
                .phoneNumber(ticketAddress.getUserPhoneNumber())
                .address(ticketAddress.getUserAddress1())
                .detail(ticketAddress.getUserAddress2())
                .build();
    }

    private String generateReserveSMS(String concertName, String seatInfo, LocalDateTime concertDateTime) {
        // 공연 날짜 및 시간 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

        // 메시지 내용 생성
        return String.format(
                """
                        [티케타카]
                        공연명: %s
                        좌석 정보: %s
                        공연 일시: %s

                        티켓 확인 링크: %s""",
                concertName,
                seatInfo,
                concertDateTime.format(formatter),
                "https://ticketaka.shop/member/tickets"
        );
    }
}
