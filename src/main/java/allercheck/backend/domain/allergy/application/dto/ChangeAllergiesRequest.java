package allercheck.backend.domain.allergy.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAllergiesRequest {
    private List<String> allergies;
}
