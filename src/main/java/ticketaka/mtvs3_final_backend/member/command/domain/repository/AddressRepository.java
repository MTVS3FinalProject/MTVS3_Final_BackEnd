package ticketaka.mtvs3_final_backend.member.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
