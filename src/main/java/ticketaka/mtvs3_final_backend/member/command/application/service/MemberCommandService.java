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

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberCommandService {


    private final TicketAddressRedisRepository ticketAddressRedisRepository;
    private final AddressRepository addressRepository;

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
