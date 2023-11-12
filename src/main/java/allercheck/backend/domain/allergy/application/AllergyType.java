package allercheck.backend.domain.allergy.application;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum AllergyType {
    CRUSTACEAN("갑각류"),
    EGG("달걀"),
    FISH("생선"),
    MILK("유제품"),
    NUTS("견과류"),
    WHEAT("밀"),
    MEAT("육류"),
    FRUIT("과일류");

    private final String name;

    public static AllergyType nameOf(String name) {
        for (AllergyType allergyType : AllergyType.values()) {
            if (allergyType.getName().equals(name)) {
                return allergyType;
            }
        }
        return null;
    }
}
