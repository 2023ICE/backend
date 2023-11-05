package allercheck.backend.auth.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignInRequest;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.auth.exception.InvalidNameFormatException;
import allercheck.backend.domain.auth.exception.InvalidPasswordFormatException;
import allercheck.backend.domain.auth.exception.InvalidUsernameFormatException;
import allercheck.backend.domain.auth.exception.PasswordAndCheckedPasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.PasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.UsernameAlreadyExistsException;
import allercheck.backend.domain.auth.exception.UsernameNotEqualsException;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.repository.MemberRepository;
import allercheck.backend.global.jwt.TokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Test
    void 회원가입을_한다() {
        // Given
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313!"
                ,"daily1313!", "김승범");
        Member member = Member.createMember(memberSignUpRequest.getUsername(),
                memberSignUpRequest.getPassword(), memberSignUpRequest.getName());
        given(memberRepository.existsByUsername(anyString())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // When
        Member createdMember = authService.signUp(memberSignUpRequest);

        // Then
        assertThat(createdMember.getUsername()).isEqualTo(memberSignUpRequest.getUsername());
        assertThat(createdMember.getName()).isEqualTo(memberSignUpRequest.getName());
    }

    @Test
    void 로그인을_한다() {
        // Given
        String token = "token";
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        MemberSignInRequest memberSignInRequest = new MemberSignInRequest(member.getUsername(), member.getPassword());
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));
        given(tokenProvider.createToken(anyString())).willReturn(token);

        // When
        String accessToken = authService.signIn(memberSignInRequest);

        // Then
        assertThat(accessToken).isEqualTo(token);
    }

    @Test
    void 이메일_값이_공백_이면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("", "daily1313!", "daily1313!", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidUsernameFormatException.class);
    }

    @Test
    void 이메일_형식에_맞지_않으면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218", "daily1313!", "daily1313!", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidUsernameFormatException.class);
    }

    @Test
    void 패스워드_값이_공백_이면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "", "", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }

    @Test
    void 패스워드_값이_형식에_맞지_않으면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313", "daily1313", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }

    @Test
    void 패스워드_값이_확인용_패스워드_값과_다르면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313!", "daily1313", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(PasswordAndCheckedPasswordNotEqualsException.class);
    }

    @Test
    void 이름이_공백_이면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313!", "daily1313!", "");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidNameFormatException.class);
    }

    @Test
    void 이름이_두_글자_이상_네_글자_이하_가_아니면_예외가_발생_한다() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313!", "daily1313!", "김호랑나비");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(InvalidNameFormatException.class);
    }

    @Test
    void 회원가입_할때_이미_가입된_이메일_이면_예외가_발생_한다() {
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        given(memberRepository.existsByUsername(member.getUsername())).willReturn(true);

        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7218@naver.com", "daily1313!", "daily1313!", "김승범");

        assertThatThrownBy(() -> authService.signUp(memberSignUpRequest))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void 로그인_할때_비밀번호가_다르면_예외가_발생_한다() {
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        MemberSignInRequest memberSignInRequest = new MemberSignInRequest("kimsb7218@naver.com","abcd1234!");

        assertThatThrownBy(() -> authService.signIn(memberSignInRequest))
                .isInstanceOf(PasswordNotEqualsException.class);
    }

    @Test
    void 로그인_할때_가입된_이메일이_없으면_예외가_발생_한다() {
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        MemberSignInRequest memberSignInRequest = new MemberSignInRequest("kimsb7219@naver.com","daily1313!");

        assertThatThrownBy(() -> authService.signIn(memberSignInRequest))
                .isInstanceOf(UsernameNotEqualsException.class);
    }
}
