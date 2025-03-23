package ticketaka.mtvs3_final_backend.member.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ticketaka.mtvs3_final_backend.member.query.dao.MemberStickerData;

import java.util.Optional;

@Repository
public interface MemberStickerDataRepository extends MongoRepository<MemberStickerData, String> {

    Optional<MemberStickerData> findByMemberId(Long memberId);
}
