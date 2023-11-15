package allercheck.backend.domain.allergy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckRecipeResponse {

    private String name;
    private String imageUrl;
    private Set<String> ingredients;
    private Set<String> causes;

    public static CheckRecipeResponse toDto(String name, String imageUrl, Set<String> ingredients, Set<String> causes) {
        return new CheckRecipeResponse(name, imageUrl, ingredients, causes);
    }
}
