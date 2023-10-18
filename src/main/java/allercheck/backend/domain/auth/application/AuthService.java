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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signUp(final MemberSignUpRequest memberSignUpRequest) {
        validateSignUpInfo(memberSignUpRequest);
        validateDuplicatedUsername(memberSignUpRequest);
        validateCheckedPassword(memberSignUpRequest.getPassword(), memberSignUpRequest.getCheckedPassword());
        Member member = Member.createMember(memberSignUpRequest.getUsername(),
                memberSignUpRequest.getPassword(), memberSignUpRequest.getName());
        memberRepository.save(member);
        return MemberResponse.toDto(member);
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

    private void validateSignUpInfo(final MemberSignUpRequest memberSignUpRequest) {
        validateUsernameFormat(memberSignUpRequest.getUsername());
        validatePasswordFormat(memberSignUpRequest.getPassword());
        validateNameFormat(memberSignUpRequest.getName());
    }

    private void validateUsernameFormat(final String username) {
        if(username == null || !username.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidUsernameFormatException();
        }
    }

    private void validatePasswordFormat(final String password) {
        if(password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new InvalidPasswordFormatException();
        }
    }

    private void validateNameFormat(final String name) {
        if(name == null || !(name.length() >= 2 && name.length() <= 4)) {
            throw new InvalidNameFormatException();
        }
    }
}
