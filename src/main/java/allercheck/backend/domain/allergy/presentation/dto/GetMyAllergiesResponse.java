package allercheck.backend.domain.allergy.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@AllArgsConstructor
@Data
public class GetMyAllergiesResponse {
    private List<String> allergies;

    public static GetMyAllergiesResponse toDto(List<String> allergies) {
        return new GetMyAllergiesResponse(allergies);
    }
}
