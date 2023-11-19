package allercheck.backend.domain.member.presentation;

import allercheck.backend.domain.member.application.MemberService;
import allercheck.backend.domain.member.application.dto.MemberPasswordEditRequest;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.web.resolver.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@AuthMember final Member member,
                                                 @Valid @RequestBody final MemberPasswordEditRequest memberPasswordEditRequest) {
        memberService.changePassword(member, memberPasswordEditRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
