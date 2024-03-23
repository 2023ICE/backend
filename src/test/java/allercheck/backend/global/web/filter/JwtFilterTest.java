package allercheck.backend.global.web.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import allercheck.backend.global.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private TokenProvider tokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void 경로가_화이트리스트면_필터체인을_통과한다() throws ServletException, IOException {
        // given
        FilterChain filterChain = mock(FilterChain.class);
        request.setRequestURI("/test");

        // when
        jwtFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    public void 토큰이_Bearer로_시작하지_않으면_401_에러를_반환한다() throws ServletException, IOException {
        // given
        FilterChain filterChain = mock(FilterChain.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("not bearer token");

        // when
        jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        verify(httpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
    }

    @Test
    public void 토큰이_Bearer로_시작하면_필터체인을_통과한다() throws ServletException, IOException {
        // given
        FilterChain filterChain = mock(FilterChain.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer Token");

        // when
        jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        verify(filterChain).doFilter(any(), any());
    }
}
