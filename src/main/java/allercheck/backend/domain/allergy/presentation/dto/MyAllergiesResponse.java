package allercheck.backend.domain.allergy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyAllergiesResponse {

    private List<String> allergies;

    public static MyAllergiesResponse toDto(List<String> allergies) {
        return new MyAllergiesResponse(allergies);
    }
}
