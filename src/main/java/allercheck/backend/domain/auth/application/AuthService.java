package allercheck.backend.domain.auth.application;

import allercheck.backend.domain.auth.exception.InvalidNameFormatException;
import allercheck.backend.domain.auth.exception.InvalidPasswordFormatException;
import allercheck.backend.domain.auth.exception.InvalidUsernameFormatException;
import allercheck.backend.domain.auth.presentation.dto.MemberResponse;
import allercheck.backend.domain.auth.presentation.dto.TokenResponse;
import allercheck.backend.domain.auth.application.dto.MemberSignInRequest;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.auth.exception.PasswordAndCheckedPasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.UsernameAlreadyExistsException;
import allercheck.backend.domain.member.domain.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import allercheck.backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signUp(final MemberSignUpRequest memberSignUpRequest) {
        validateDuplicatedUsername(memberSignUpRequest);
        validateCheckedPassword(memberSignUpRequest.getPassword(), memberSignUpRequest.getCheckedPassword());
        Member member = Member.createMember(memberSignUpRequest.getUsername(),
                memberSignUpRequest.getPassword(), memberSignUpRequest.getName());
        memberRepository.save(member);
        return MemberResponse.toDto(member);
    }

    @Transactional
    public TokenResponse signIn(final MemberSignInRequest memberSignInRequest) {
        Member member = memberRepository.findByUsername(memberSignInRequest.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        member.validateSignInInfo(memberSignInRequest.getUsername(), memberSignInRequest.getPassword());
        member.validatePassword(member.getPassword());
        return new TokenResponse(tokenProvider.createToken(String.valueOf(member.getId())));
    }

    public Member extractMember(final String accessToken) {
        Long memberId = Long.valueOf(tokenProvider.getPayLoad(accessToken));
        validateMemberId(memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void validateDuplicatedUsername(final MemberSignUpRequest memberSignUpRequest) {
        if(memberRepository.existsByUsername(memberSignUpRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
    }

    private void validateCheckedPassword(final String password, final String checkedPassword) {
        if(!password.equals(checkedPassword)) {
            throw new PasswordAndCheckedPasswordNotEqualsException();
        }
    }

    public void validateMemberId(final Long memberId) {
        if(!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }
}
