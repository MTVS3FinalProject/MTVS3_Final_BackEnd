package ticketaka.mtvs3_final_backend.member.query.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "member_sticker")
public class MemberStickerDAO {

    @Id
    private String id;

    @Indexed
    private Long memberId;
    private Long stickerId;

    private String stickerName;
    private String stickerScript;
    private String stickerRarity;
    private String stickerImage;
}
