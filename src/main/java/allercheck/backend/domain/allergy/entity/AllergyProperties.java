package allercheck.backend.domain.allergy.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.util.Map;


@Getter
@PropertySource("classpath:allergy.properties")
@Component
@Slf4j
public class AllergyProperties {

    private final Map<String, AllergyType> allergenicIngredients;
    public AllergyProperties(@Value("#{${allergenicIngredientsMap}}") Map<String, AllergyType> allergenicIngredientsMap) {
        this.allergenicIngredients = Map.copyOf(allergenicIngredientsMap);
    }

    public AllergyType getAllergyByIngredient(String ingredient) {
        return allergenicIngredients.get(ingredient);
    }

    public boolean isAllergenicIngredient(String ingredient) {
        return allergenicIngredients.containsKey(ingredient);
    }
}
