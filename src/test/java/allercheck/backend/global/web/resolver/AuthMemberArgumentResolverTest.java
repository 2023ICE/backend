package allercheck.backend.global.web.resolver;

import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.web.resolver.annotation.AuthMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AuthMemberArgumentResolverTest {

    @InjectMocks
    private AuthMemberArgumentResolver resolver;

    @Mock
    private AuthService authService;

    private MockHttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        httpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public void 매개변수에_AuthMember_어노테이션이_있으면_true를_반환한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(AuthMember.class)).thenReturn(true);

        // when
        boolean hasAuthMemberAnnotation = resolver.supportsParameter(parameter);

        // then
        assertTrue(hasAuthMemberAnnotation);
    }

    @Test
    public void 매개변수에_AuthMember_어노테이션이_없으면_false를_반환한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(AuthMember.class)).thenReturn(false);

        // when
        boolean hasAuthMemberAnnotation = resolver.supportsParameter(parameter);

        // then
        assertFalse(hasAuthMemberAnnotation);
    }

    @Test
    public void 매개변수에_AuthMember_어노테이션이_있으면_유저정보를_가져온다() throws Exception {
        // given
        String accessToken = "accessToken";
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "위진영");
        httpServletRequest.addHeader("Authorization", "Bearer " + accessToken);
        NativeWebRequest webRequest = new ServletWebRequest(httpServletRequest);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        MethodParameter parameter = mock(MethodParameter.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);

        when(authService.extractMember(accessToken)).thenReturn(member);

        // when
        Object result = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        // then
        assertEquals(member, result);
    }
}
