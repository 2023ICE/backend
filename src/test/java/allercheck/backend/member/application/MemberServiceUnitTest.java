package allercheck.backend.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import allercheck.backend.domain.auth.exception.NewPasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.PresentPasswordNotEqualsException;
import allercheck.backend.domain.member.application.MemberService;
import allercheck.backend.domain.member.application.dto.MemberPasswordEditRequest;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
class MemberServiceUnitTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        when(memberRepository.findByUsername(member.getUsername())).thenReturn(Optional.of(member));
    }

    @Test
    void 비밀번호를_변경한다() {
        //given
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        MemberPasswordEditRequest memberPasswordEditRequest = new MemberPasswordEditRequest("daily1313!", "daily1414!", "daily1414!");

        //when
        memberService.changePassword(currentMember, memberPasswordEditRequest);

        //then
        assertThat(member.getPassword()).isEqualTo(memberPasswordEditRequest.getNewPassword());
    }

    @Test
    void 현재_비밀번호가_일치하지_않으면_예외가_발생한다() {
        //given
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        MemberPasswordEditRequest memberPasswordEditRequest = new MemberPasswordEditRequest("daily1316!", "daily1414!", "daily1414!");

        //when, then
        assertThatThrownBy(() -> memberService.changePassword(currentMember, memberPasswordEditRequest))
                .isInstanceOf(PresentPasswordNotEqualsException.class);
    }

    @Test
    void 새로운_비밀번호와_확인용_비밀번호가_다르면_예외가_발생한다() {
        //given
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        MemberPasswordEditRequest memberPasswordEditRequest = new MemberPasswordEditRequest("daily1313!", "daily1414!", "daily1415!");

        //when, then
        assertThatThrownBy(() -> memberService.changePassword(currentMember, memberPasswordEditRequest))
                .isInstanceOf(NewPasswordNotEqualsException.class);
    }
}
