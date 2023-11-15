package allercheck.backend.domain.allergy.entity;

import allercheck.backend.domain.allergy.exception.InvalidAllergyTypeNameException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@AllArgsConstructor
@Getter
public enum AllergyType {

    CRUSTACEAN("갑각류"),
    EGG("난류"),
    FISH("생선"),
    MILK("유제품"),
    NUTS("견과류"),
    WHEAT("밀"),
    MEAT("육류"),
    FRUIT("과일류");

    private final String name;

    public static AllergyType nameOf(String name) {
        return Arrays.stream(AllergyType.values())
                .filter(allergyType -> allergyType.getName().equals(name))
                .findFirst()
                .orElseThrow(InvalidAllergyTypeNameException::new);
    }
}
