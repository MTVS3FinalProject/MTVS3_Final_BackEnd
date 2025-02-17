package ticketaka.mtvs3_final_backend.member.query.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ticketaka.mtvs3_final_backend.member.query.dto.getMemberTitleDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Document(collection = "member_title")
public class MemberTitleData {

    @Id
    private String id;

    @Indexed
    private Long memberId;

    private List<Title> titleList;

    @Getter
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

        public getMemberTitleDTO toDTO() {
            return new getMemberTitleDTO(titleId, titleName, titleScript, titleRarity);
        }
    }

    @Builder
    public MemberTitleData(Long memberId, List<Title> titleList) {
        this.memberId = memberId;
        this.titleList = (titleList != null) ? new ArrayList<>(titleList) : new ArrayList<>();
    }

    public void addTitle(Title newTitle) {
        this.titleList.add(newTitle);
    }

    public List<getMemberTitleDTO> toTitleDTOList() {
        return titleList.stream().map(Title::toDTO).toList();
    }
}
