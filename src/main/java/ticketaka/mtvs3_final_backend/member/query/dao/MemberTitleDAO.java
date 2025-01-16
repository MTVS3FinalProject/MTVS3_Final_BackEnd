package ticketaka.mtvs3_final_backend.member.query.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "member_title")
public class MemberTitleDAO {

    @Id
    private String id;

    @Indexed
    private Long memberId;
    private Long titleId;

    private String titleName;
    private String titleScript;
    private String titleRarity;
    private Boolean isRepresentative;
}
