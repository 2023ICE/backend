package allercheck.backend.global.web.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import allercheck.backend.domain.auth.exception.InvalidTokenException;
import allercheck.backend.global.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SuppressWarnings("NonAsciiCharacters")
@TestPropertySource(locations = "classpath:secure.properties")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
public class TokenProviderTest {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expire-length}")
    private long validityTime;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(secretKey, validityTime);
    }

    @Test
    public void jwt_토큰을_생성한다() {
        //given
        String payload = String.valueOf(1L);

        // when
        String token = tokenProvider.createToken(payload);

        // then
        assertThat(token).isNotEmpty();
    }

    @Test
    public void 토큰_정보로_payload를_조회한다() {
        //given
        String payload = String.valueOf(1L);
        String token = tokenProvider.createToken(payload);

        //when & then
        assertThat(tokenProvider.getPayLoad(token)).isEqualTo(payload);
    }

    @Test
    public void 토큰에_null값이_들어가면_예외가_발생한다() {
        // then
        assertThatThrownBy(() -> tokenProvider.validateToken(null))
                .isInstanceOf(InvalidTokenException.class);
    }
}
