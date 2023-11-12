package allercheck.backend.domain.allergy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class CheckedFood {
    private String name;
    private String imageUrl;
    private Set<String> ingredient;
    private Set<String> cause;

    public static CheckedFood createCheckedFood(String name, String imageUrl, Set<String> ingredient, Set<String> cause) {
        return new CheckedFood(name, imageUrl, ingredient, cause);
    }
}
