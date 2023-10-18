package allercheck.backend.global.web.filter;

import allercheck.backend.domain.auth.exception.InvalidTokenException;
import allercheck.backend.global.jwt.TokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtFilter implements Filter {

    private static final String[] whiteList = { "/", "/api/auth/sign-in", "/api/auth/sign-up", "/css/*"};

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        log.info("call doFilter method >>>");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if(!isLoginCheckPath(requestUri)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            tokenProvider.validateToken(token);
            chain.doFilter(httpRequest, httpResponse);
        } catch (InvalidTokenException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    private boolean isLoginCheckPath(final String requestUri) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestUri);
    }
}
