package allercheck.backend.domain.allergy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyAllergiesResponse {

    private List<String> allergies;

    public static GetMyAllergiesResponse toDto(List<String> allergies) {
        return new GetMyAllergiesResponse(allergies);
    }
}
