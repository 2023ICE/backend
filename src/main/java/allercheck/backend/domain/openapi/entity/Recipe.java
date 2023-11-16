package allercheck.backend.domain.openapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Recipe {

    private String name;
    private String imageUrl;
    private Set<String> ingredients;
}
