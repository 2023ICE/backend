package allercheck.backend.domain.openapi.application;

import allercheck.backend.domain.openapi.entity.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> getRecipesByRecipeName(final int page, final String recipeName);
}
