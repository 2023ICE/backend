package allercheck.backend.domain.member.application;


import allercheck.backend.domain.member.application.dto.MemberPasswordEditRequest;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void changePassword(final Member member,
                                 final MemberPasswordEditRequest memberPasswordEditRequest) {
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        currentMember.changePassword(memberPasswordEditRequest.getPresentPassword(),
                memberPasswordEditRequest.getNewPassword(),
                memberPasswordEditRequest.getCheckedNewPassword());
    }
}
