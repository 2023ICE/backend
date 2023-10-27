package allercheck.backend.domain.auth.presentation.dto;

import allercheck.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Long id;
    private String username;
    private String name;

    public static MemberResponse toDto(final Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getName());
    }
}
