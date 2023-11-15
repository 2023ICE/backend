package allercheck.backend.domain.allergy.application;


import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.entity.AllergyProperties;
import allercheck.backend.domain.allergy.entity.AllergyType;
import allercheck.backend.domain.allergy.presentation.dto.CheckAllergyResponse;
import allercheck.backend.domain.allergy.presentation.dto.CheckRecipeResponse;
import allercheck.backend.domain.allergy.presentation.dto.GetMyAllergiesResponse;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import allercheck.backend.domain.member.repository.MemberRepository;
import allercheck.backend.domain.openapi.entity.Recipe;
import allercheck.backend.domain.openapi.application.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AllergyService {

    private final RecipeService recipeService;
    private final AllergyProperties allergyProperties;
    private final MemberRepository memberRepository;

    public CheckAllergyResponse checkAllergyByRecipeName(Member member, int page, String recipeName) {
        List<Recipe> recipes = recipeService.getRecipesByRecipeName(page, recipeName);
        List<CheckRecipeResponse> checkRecipeResponses = new ArrayList<>();
        for (Recipe recipe : recipes) {
            checkRecipeResponses.add(checkRecipe(member.getAllergies(), recipe));
        }
        return CheckAllergyResponse.toDto(checkRecipeResponses);
    }

    @Transactional
    public GetMyAllergiesResponse changeAllergies(Member member, ChangeAllergiesRequest request) {
        Member currentMember = memberRepository.findByUsername(member.getUsername()).orElseThrow(MemberNotFoundException::new);
        EnumSet<AllergyType> newAllergies = EnumSet.noneOf(AllergyType.class);
        for (String allergy : request.getAllergies()) {
            newAllergies.add(AllergyType.nameOf(allergy));
        }
        EnumSet<AllergyType> changedAllergies = currentMember.changeAllergies(newAllergies);
        return GetMyAllergiesResponse.toDto(
                changedAllergies.stream()
                        .map(AllergyType::getName)
                        .toList());
    }

    @Transactional(readOnly = true)
    public GetMyAllergiesResponse getMyAllergies(Member member) {
        return GetMyAllergiesResponse.toDto(
                member.getAllergies()
                        .stream()
                        .map(AllergyType::getName)
                        .toList());
    }

    private CheckRecipeResponse checkRecipe(EnumSet<AllergyType> memberAllergies, Recipe recipe) {
        Pair<Set<String>, Set<String>> detectedIngredientsAndCauses = detectIngredientsAndCauses(memberAllergies, recipe.getIngredients());
        return CheckRecipeResponse.toDto(
                recipe.getName(),
                recipe.getImageUrl(),
                detectedIngredientsAndCauses.getFirst(),
                detectedIngredientsAndCauses.getSecond());
    }

    private Pair<Set<String>, Set<String>> detectIngredientsAndCauses(EnumSet<AllergyType> memberAllergies, Set<String> ingredients) {
        Set<String> detectedIngredients = new LinkedHashSet<>();
        Set<String> causes = new LinkedHashSet<>();
        for (String ingredient : ingredients) {
            if (canCauseAllergy(memberAllergies, ingredient)) {
                detectedIngredients.add(ingredient);
                causes.add(allergyProperties.getAllergyByIngredient(ingredient).getName());
            }
        }
        return Pair.of(detectedIngredients, causes);
    }

    private boolean canCauseAllergy(EnumSet<AllergyType> memberAllergies, String ingredient) {
        if (allergyProperties.isAllergenicIngredient(ingredient)) {
            return memberAllergies.contains(allergyProperties.getAllergyByIngredient(ingredient));
        }
        return false;
    }
}
