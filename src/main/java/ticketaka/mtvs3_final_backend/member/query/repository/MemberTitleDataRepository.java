package ticketaka.mtvs3_final_backend.member.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.query.dao.MemberTitleData;

import java.util.Optional;

@Repository
public interface MemberTitleDataRepository extends MongoRepository<MemberTitleData, String> {

    Optional<MemberTitleData> findByMemberId(Long memberId);
}
