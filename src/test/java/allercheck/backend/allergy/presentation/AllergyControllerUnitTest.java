package allercheck.backend.allergy.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import allercheck.backend.domain.allergy.application.AllergyService;
import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.entity.AllergyType;
import allercheck.backend.domain.allergy.presentation.AllergyController;
import allercheck.backend.domain.allergy.presentation.dto.MyAllergiesResponse;
import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.jwt.TokenProvider;
import allercheck.backend.global.web.interceptor.JwtInterceptor;
import allercheck.backend.global.web.resolver.AuthMemberArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AllergyController.class)
public class AllergyControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AllergyService allergyService;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtInterceptor jwtInterceptor;

    @MockBean
    private AuthMemberArgumentResolver authMemberArgumentResolver;

    @Test
    public void 내_알러지_정보를_반환한다() throws Exception {
        //given
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        List<String> allergies = List.of("갑각류", "난류", "생선", "유제품", "밀", "육류", "과일류");
        MyAllergiesResponse myAllergiesResponse = new MyAllergiesResponse(allergies);

        when(allergyService.getMyAllergies(any(Member.class))).thenReturn(myAllergiesResponse);

        //when & then
        mockMvc.perform(get("/api/allergy", member))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.allergies").isArray())
//                .andExpect(jsonPath("$.allergies", hasSize(allergies.size())))
//                .andExpect(jsonPath("$.allergies", hasItems("갑각류", "난류", "생선", "유제품", "견과류", "밀", "육류", "과일류")));
    }

    @Test
    public void 알러지_정보를_추가한다() throws Exception {
        //given
        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        EnumSet<AllergyType> allAllergiesTypes = EnumSet.of(AllergyType.CRUSTACEAN, AllergyType.EGG, AllergyType.FISH, AllergyType.MILK, AllergyType.NUTS, AllergyType.WHEAT, AllergyType.MEAT, AllergyType.FRUIT);
        List<String> allergies = List.of("갑각류", "난류", "생선", "유제품" ,"견과류", "밀", "육류", "과일류");
        ChangeAllergiesRequest changeAllergiesRequest = new ChangeAllergiesRequest(allergies);
        String content = objectMapper.writeValueAsString(changeAllergiesRequest);

        EnumSet<AllergyType> newAllergies = changeAllergiesRequest.getAllergies().stream()
                .map(AllergyType::nameOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(AllergyType.class)));

        member.changeAllergies(newAllergies);

        doNothing().when(allergyService).changeAllergies(member, changeAllergiesRequest);

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/allergy", member, changeAllergiesRequest)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
