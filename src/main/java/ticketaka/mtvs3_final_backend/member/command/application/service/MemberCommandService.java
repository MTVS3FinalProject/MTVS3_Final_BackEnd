package ticketaka.mtvs3_final_backend.member.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.AddressRepository;
import ticketaka.mtvs3_final_backend.redis.ticketaddress.domain.TicketAddress;
import ticketaka.mtvs3_final_backend.redis.ticketaddress.repository.TicketAddressRedisRepository;
import ticketaka.mtvs3_final_backend.title.member.command.domain.model.MemberTitle;
import ticketaka.mtvs3_final_backend.title.member.command.domain.repository.MemberTitleCommandRepository;
import ticketaka.mtvs3_final_backend.title.member.query.repository.MemberTitleQueryRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final AddressRepository addressRepository;
    private final MemberTitleCommandRepository memberTitleCommandRepository;
    private final MemberTitleQueryRepository memberTitleQueryRepository;

    private final TicketAddressRedisRepository ticketAddressRedisRepository;

    /*
            티켓 주소지 입력
         */
    public void saveTicketAddress(Long memberId, Long concertId, Long seatId, Long ticketId) {
        TicketAddress ticketAddress = ticketAddressRedisRepository.findById(TicketAddress.generateTicketAddressId(
                memberId, concertId, seatId)
        ).orElseThrow(() -> new Exception400("주소지 입력이 되지 않았습니다."));

        Address address = newAddress(ticketAddress, memberId, ticketId);
        addressRepository.save(address);

        ticketAddressRedisRepository.delete(ticketAddress);
    }

    /*
        메인 타이틀 변경
     */
    public void changeMainTitle(Long memberId, Long titleId) {

        // 기존 Main Title 해제
        memberTitleCommandRepository.clearRepresentativeTitle(memberId);

        // Main Title 설정
        MemberTitle memberTitle = memberTitleQueryRepository.findByMemberIdAndTitleId(memberId, titleId)
                .orElseThrow(() -> new Exception400("해당 Title 을 소유하고 있지 않습니다."));
        memberTitle.setIsRepresentative(true);
        memberTitleCommandRepository.save(memberTitle);
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
}
