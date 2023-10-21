package allercheck.backend.global.jwt;

import allercheck.backend.domain.auth.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthorizationExtractor {

    public static final String BEARER_PREFIX = "Bearer ";

    public static String extract(final HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(authenticationHeader);
        return authenticationHeader.split(" ")[1].trim();
    }

    private static void validateToken(final String authenticationHeader) {
        if(!StringUtils.hasText(authenticationHeader) || !authenticationHeader.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException();
        }
    }
}
