package allercheck.backend.allergy.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import allercheck.backend.domain.allergy.application.AllergyService;
import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.entity.AllergyProperties;
import allercheck.backend.domain.allergy.entity.AllergyType;
import allercheck.backend.domain.allergy.presentation.dto.MyAllergiesResponse;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import allercheck.backend.domain.openapi.application.RecipeService;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
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
class AllergyServiceUnitTest {

    @InjectMocks
    private AllergyService allergyService;

    @Mock
    private RecipeService recipeService;

    @Mock
    private AllergyProperties allergyProperties;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
        when(memberRepository.findByUsername(member.getUsername())).thenReturn(Optional.of(member));
    }

    @Test
    void 내_알러지_정보를_반환한다() {
        //given
        List<String> allAllergies = List.of("갑각류", "난류", "생선", "유제품" ,"견과류", "밀", "육류", "과일류");
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        ChangeAllergiesRequest changeAllergiesRequest = new ChangeAllergiesRequest(allAllergies);
        allergyService.changeAllergies(currentMember, changeAllergiesRequest);

        //when
        MyAllergiesResponse myAllergies = allergyService.getMyAllergies(currentMember);

        //then
        assertThat(myAllergies.getAllergies()).isEqualTo(allAllergies);
    }

    @Test
    void 내_알러지_정보를_추가한다() {
        //given
        EnumSet<AllergyType> allAllergiesTypes = EnumSet.of(AllergyType.CRUSTACEAN, AllergyType.EGG, AllergyType.FISH, AllergyType.MILK, AllergyType.NUTS, AllergyType.WHEAT, AllergyType.MEAT, AllergyType.FRUIT);
        List<String> allAllergiesNameList = List.of("갑각류", "난류", "생선", "유제품" ,"견과류", "밀", "육류", "과일류");
        Member currentMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        ChangeAllergiesRequest changeAllergiesRequest = new ChangeAllergiesRequest(allAllergiesNameList);

        //when
        EnumSet<AllergyType> newAllergies = changeAllergiesRequest.getAllergies().stream()
                .map(AllergyType::nameOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(AllergyType.class)));

        currentMember.changeAllergies(newAllergies);
        allergyService.changeAllergies(currentMember, changeAllergiesRequest);

        //then
        assertThat(currentMember.getAllergies()).isEqualTo(allAllergiesTypes);
    }

    @Test
    void 음식의_재료를_쿼리파라미터로_넘겨주면_알러지_검출_결과를_반환한다() {
        //given

    }
}
