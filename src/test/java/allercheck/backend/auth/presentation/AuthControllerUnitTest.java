package allercheck.backend.auth.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignInRequest;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.auth.presentation.AuthController;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.jwt.TokenProvider;
import allercheck.backend.global.web.interceptor.JwtInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtInterceptor jwtInterceptor;

    @Test
    void 회원가입을_하면_멤버_응답_디티오를_반환한다() throws Exception {
        //given
        Member member = Member.createMember("kimsb7219@naver.com", "daily1313!","김승범");
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("kimsb7219@naver.com", "daily1313!",
                "daily1313!","김승범");
        String content = objectMapper.writeValueAsString(memberSignUpRequest);
        when(authService.signUp(any(MemberSignUpRequest.class))).thenReturn(member);

        //when
        ResultActions resultAction = mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("kimsb7219@naver.com"))
                .andExpect(jsonPath("$.name").value("김승범"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 로그인을_하면_회원_이름과_엑세스_토큰을_반환한다() throws Exception {
        //given
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjk4NDIyMDQwLCJleHAiOjE2OTg0NTgwNDB9.NYTXyO7DW8rl4WoZUDLUHARTleCl56vhlyc3Lm56XHw";
        MemberSignInRequest memberSignInRequest = new MemberSignInRequest("kimsb7218@naver.com", "daily1313!");
        String content = objectMapper.writeValueAsString(memberSignInRequest);
        when(authService.signIn(any(MemberSignInRequest.class))).thenReturn(Pair.of(anyString(),accessToken));

        //when
        ResultActions resultAction = mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andDo(MockMvcResultHandlers.print());
    }
}
