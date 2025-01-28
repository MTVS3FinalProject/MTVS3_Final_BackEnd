package ticketaka.mtvs3_final_backend.member.query.dao;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "member_title")
public class MemberTitleDAO {

    @Id
    private String id;

    @Indexed
    private Long memberId;

    private List<Title> titleList = List.of();

    @Data
    @NoArgsConstructor
    public static class Title {
        private Long titleId;
        private String titleName;
        private String titleScript;
        private String titleRarity;

        @Builder
        public Title(Long titleId, String titleName, String titleScript, String titleRarity) {
            this.titleId = titleId;
            this.titleName = titleName;
            this.titleScript = titleScript;
            this.titleRarity = titleRarity;
        }
    }

    @Builder
    public MemberTitleDAO(Long memberId, List<Title> titleList) {
        this.memberId = memberId;
        this.titleList = titleList;
    }
}
