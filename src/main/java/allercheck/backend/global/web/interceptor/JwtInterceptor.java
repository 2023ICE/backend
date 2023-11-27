package allercheck.backend.global.web.interceptor;

import allercheck.backend.global.jwt.AuthorizationExtractor;
import allercheck.backend.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private static final String PREFLIGHT_OPTIONS_METHOD = "OPTIONS";

    private final AuthorizationExtractor authorizationExtractor;
    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if(request.getMethod().equals(PREFLIGHT_OPTIONS_METHOD)) {
            return true;
        }

        log.info("call preHandle method >> ");
        String extractedToken = authorizationExtractor.extract(request);
        String name = tokenProvider.getPayLoad(extractedToken);
        request.setAttribute("name", name);
        return true;
    }
}
