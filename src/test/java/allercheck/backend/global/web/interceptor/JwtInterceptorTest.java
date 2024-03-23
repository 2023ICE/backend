package allercheck.backend.global.web.interceptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import allercheck.backend.global.jwt.AuthorizationExtractor;
import allercheck.backend.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class JwtInterceptorTest {

    @InjectMocks
    private JwtInterceptor jwtInterceptor;

    @Mock
    private AuthorizationExtractor authorizationExtractor;

    @Mock
    private Object handler;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;


    @Test
    public void preflight_요청이_들어오면_true를_반환한다() throws Exception {
        // given
        when(request.getMethod()).thenReturn("OPTIONS");
        when(request.getHeader("Access-Control-Request-Method")).thenReturn("POST");
        when(request.getHeader("Origin")).thenReturn("https://allercheck-frontend.vercel.app");

        // when
        boolean result = CorsUtils.isPreFlightRequest(request);

        // then
        assertTrue(result);
    }

    @Test
    public void 토큰을_추출하고_이름을_설정한다() throws Exception {
        // given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(authorizationExtractor.extract(request)).thenReturn("Bearer token");
        when(tokenProvider.getPayLoad("token")).thenReturn("name");

        // when
        boolean result = jwtInterceptor.preHandle(request, response, handler);

        // then
        assertTrue(result);
        verify(request).setAttribute("name", "name");
    }
}
