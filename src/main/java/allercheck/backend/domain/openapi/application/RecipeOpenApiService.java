package allercheck.backend.domain.openapi.application;


import allercheck.backend.domain.openapi.entity.Recipe;
import allercheck.backend.domain.openapi.presentaion.RecipeOpenApiResponse;
import allercheck.backend.domain.openapi.exception.OpenApiConnectionFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class RecipeOpenApiService implements RecipeService {

    @Value("${openapi.url}")
    private String RECIPE_OPENAPI_BASE_URL;
    @Value("${openapi.secret}")
    private String RECIPE_OPENAPI_SECRET;
    private static final String RECIPE_OPENAPI_SERVICE_NAME = "COOKRCP01";
    private static final String RECIPE_OPENAPI_FILE_TYPE = "json";
    private static final int PAGE_POLICY = 5;
    private static final String NO_RECIPE = "INFO-200";
    private static final String SUCCESS = "INFO-000";
    private final RestTemplate restTemplate;

    @Override
    public List<Recipe> getRecipesByRecipeName(final int page, final String recipeName) {
        RecipeOpenApiResponse recipeOpenApiResponse = getRecipeOpenApiResponseByRecipeName(page, recipeName);
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeOpenApiResponse.Recipe recipe : recipeOpenApiResponse.getCOOKRCP01().getRow()) {
            Set<String> ingredients = convertIngredientsStringToSet(recipe.getPartsDetails());
            recipes.add(new Recipe(recipe.getRecipeName(), recipe.getImagePathBig(), ingredients));
        }
        return recipes;
    }

    private RecipeOpenApiResponse getRecipeOpenApiResponseByRecipeName(final int page, final String recipeName) {
        URI openApiRequestUri = buildOpenApiRequestUri(page, recipeName);
        ResponseEntity<RecipeOpenApiResponse> response = restTemplate.getForEntity(openApiRequestUri, RecipeOpenApiResponse.class);
        checkRecipeResponse(response);
        return response.getBody();
    }

    private URI buildOpenApiRequestUri(int page, String recipeName) {
        int startIdx = (page - 1) * PAGE_POLICY;
        int endIdx = page * PAGE_POLICY;
        return UriComponentsBuilder
                .fromUriString(RECIPE_OPENAPI_BASE_URL)
                .path("/{secret}/{serviceName}/{fileType}/{startIdx}/{endIdx}/{recipeName}")
                .encode()
                .build()
                .expand(RECIPE_OPENAPI_SECRET,
                        RECIPE_OPENAPI_SERVICE_NAME,
                        RECIPE_OPENAPI_FILE_TYPE,
                        startIdx,
                        endIdx,
                        "RCP_NM=" + recipeName)
                .toUri();
    }

    private void checkRecipeResponse(ResponseEntity<RecipeOpenApiResponse> response) {
        String responseCode = response.getBody().getCOOKRCP01().getRESULT().getCODE();
        if (!response.getStatusCode().is2xxSuccessful() || (!responseCode.equals(NO_RECIPE) && !responseCode.equals(SUCCESS))) {
            throw new OpenApiConnectionFailureException();
        }
    }

    private Set<String> convertIngredientsStringToSet(String ingredients) {
        Set<String> list = new LinkedHashSet<>();
        Pattern pattern = Pattern.compile("[가-힣]+");
        Matcher matcher = pattern.matcher(ingredients);

        while (matcher.find()) {
            String word = matcher.group();
            if (!word.equals("개") && !word.equals("마리") && !word.equals("컵") && !word.equals("장")) {
                list.add(word);
            }
        }
        return list;
    }
}
