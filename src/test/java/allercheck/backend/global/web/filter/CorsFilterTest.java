package allercheck.backend.global.web.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class CorsFilterTest {

    @InjectMocks
    private CorsFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Test
    public void CORS_헤더를_설정하고_FilterChain으로_전달한다() throws IOException, ServletException {
        // given
        when(request.getMethod()).thenReturn("GET");

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(response).setHeader("Access-Control-Allow-Origin", "https://allercheck-frontend.vercel.app");
        verify(response).setHeader("Access-Control-Allow-Credentials", "true");
        verify(response).setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE, PATCH");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, Authorization");
        verify(chain).doFilter(request, response);
    }

    @Test
    public void OPTIONS_메서드로_요청하면_SC_OK_응답을_반환한다() throws IOException, ServletException {
        // given
        when(request.getMethod()).thenReturn("OPTIONS");

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(chain, never()).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
