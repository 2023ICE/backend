package allercheck.backend.member.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.member.application.MemberService;
import allercheck.backend.domain.member.application.dto.MemberPasswordEditRequest;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.presentation.MemberController;
import allercheck.backend.global.jwt.AuthorizationExtractor;
import allercheck.backend.global.jwt.TokenProvider;
import allercheck.backend.global.web.interceptor.JwtInterceptor;
import allercheck.backend.global.web.resolver.AuthMemberArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MemberController.class)
class MemberControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtInterceptor jwtInterceptor;

    @Test
    void 비밀번호를_변경한다() throws Exception {
        //given
        Member member = Member.createMember("kimsb7219@naver.com", "daily1313!","김승범");
        MemberPasswordEditRequest memberPasswordEditRequest = new MemberPasswordEditRequest("daily1313!", "daily1414!", "daily1414!");
        String content = objectMapper.writeValueAsString(memberPasswordEditRequest);
        doNothing().when(memberService).changePassword(any(Member.class), any(MemberPasswordEditRequest.class));

        //when
        ResultActions resultAction = mockMvc.perform(patch("/api/member/change-password", member, memberPasswordEditRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
