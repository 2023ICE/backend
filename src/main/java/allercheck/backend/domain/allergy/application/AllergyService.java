package allercheck.backend.domain.allergy.application;


import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.presentation.dto.CheckAllergyResponse;
import allercheck.backend.domain.allergy.presentation.dto.CheckedFood;
import allercheck.backend.domain.allergy.presentation.dto.GetMyAllergiesResponse;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import allercheck.backend.domain.openapi.presentaion.GetRecipeResponse;
import allercheck.backend.domain.openapi.application.RecipeOpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class AllergyService {

    private final RecipeOpenApiService recipeOpenApiService;
    private final AllergyProperties allergyProperties;
    private final MemberRepository memberRepository;

    public CheckAllergyResponse checkAllergy(Member member, int page, String recipeName) {
        GetRecipeResponse response = recipeOpenApiService.getRecipeResponse(page, recipeName);
        CheckAllergyResponse checkAllergyResponse = new CheckAllergyResponse();
        for (GetRecipeResponse.Recipe recipe : response.getCOOKRCP01().getRow()) {
            String name = recipe.getRecipeName();
            String imageUrl = recipe.getImagePathBig();
            Set<String> ingredients = convertStringToSet(recipe.getPartsDetails());
            Pair<Set<String>, Set<String>> ingredientsAndCauses = findCauses(member.getAllergies(), ingredients);
            CheckedFood checkedFood = CheckedFood.createCheckedFood(name, imageUrl, ingredientsAndCauses.getFirst(), ingredientsAndCauses.getSecond());
            checkAllergyResponse.addResult(checkedFood);
        }
        return checkAllergyResponse;
    }

    @Transactional
    public GetMyAllergiesResponse changeAllergies(Member member, ChangeAllergiesRequest request) {
        Member curretnMember = memberRepository.findByUsername(member.getUsername()).orElseThrow(MemberNotFoundException::new);
        EnumSet<AllergyType> allergies = EnumSet.noneOf(AllergyType.class);
        for (String allergy : request.getAllergies()) {
            allergies.add(AllergyType.nameOf(allergy));
        }
        EnumSet<AllergyType> newAllergies = curretnMember.changeAllergies(allergies);
        return GetMyAllergiesResponse.toDto(
                newAllergies.stream()
                        .map(AllergyType::getName)
                        .toList());
    }

    @Transactional(readOnly = true)
    public GetMyAllergiesResponse getMyAllergies(Member member) {
        EnumSet<AllergyType> allergies = member.getAllergies();
        return GetMyAllergiesResponse.toDto(
                allergies.stream()
                        .map(AllergyType::getName)
                        .toList());
    }

    private Pair<Set<String>, Set<String>> findCauses(EnumSet<AllergyType> allergies, Set<String> ingredients) {
        Set<String> causes = new LinkedHashSet<>();
        Set<String> checkedIngredients = new LinkedHashSet<>();
        Map<String, AllergyType> allergenicIngredients = allergyProperties.getAllergenicIngredients();
        for (String ingredient : ingredients) {
            if (allergenicIngredients.containsKey(ingredient)) {
                AllergyType allergy = allergenicIngredients.get(ingredient);
                if (allergies.contains(allergy)) {
                    checkedIngredients.add(ingredient);
                    causes.add(allergy.getName());
                }
            }
        }
        return Pair.of(checkedIngredients, causes);
    }

    private Set<String> convertStringToSet(String target) {
        Set<String> list = new LinkedHashSet<>();
        Pattern pattern = Pattern.compile("[가-힣]+");
        Matcher matcher = pattern.matcher(target);

        while (matcher.find()) {
            String word = matcher.group();
            if (!word.equals("개") && !word.equals("마리") && !word.equals("컵") && !word.equals("장")) {
                list.add(word);
            }
        }
        return list;
    }



}
