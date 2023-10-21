package allercheck.backend.domain.auth.presentation.dto;

import allercheck.backend.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberResponse {

    private Long id;
    private String username;
    private String name;

    public static MemberResponse toDto(Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getName());
    }
}
