package allercheck.backend.domain.allergy.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAllergyResponse {

   private List<CheckRecipeResponse> result;

   public static CheckAllergyResponse toDto(List<CheckRecipeResponse> checkedRecipeRespons) {
      return new CheckAllergyResponse(checkedRecipeRespons);
   }
}

