package allercheck.backend.global.web.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import allercheck.backend.domain.auth.exception.InvalidTokenException;
import allercheck.backend.global.jwt.AuthorizationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;

@SuppressWarnings("NonAsciiCharacters")
@TestPropertySource(locations = "classpath:secure.properties")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
public class AuthorizationExtractorTest {

    @Autowired
    private AuthorizationExtractor authorizationExtractor;

    @Test
    void 액세스_토큰을_추출한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer 1234");

        // when
        String accessToken = authorizationExtractor.extract(request);

        // then
        assertThat(accessToken).isEqualTo("1234");
    }

    @Test
    void Token_값이_Bearer_접두사로_시작하면_예외가_발생한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("1234");

        //when & then
        assertThatThrownBy(() -> authorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void Token_값이_존재하지_않으면_예외가_발생한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

        //when & then
        assertThatThrownBy(() -> authorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void header가_비어있으면_예외가_발생한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("")).thenReturn("1234");

        //when & then
        assertThatThrownBy(() -> authorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void header_값이_null이면_예외가_발생한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(null)).thenReturn("1234");

        //when & then
        assertThatThrownBy(() -> authorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 액세스_토큰값이_누락되면_예외가_발생한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);

        //when & then
        assertThatThrownBy(() -> authorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }
}
