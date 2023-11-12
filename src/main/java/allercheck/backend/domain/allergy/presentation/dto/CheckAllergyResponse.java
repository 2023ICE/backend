package allercheck.backend.domain.allergy.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckAllergyResponse {

   private List<CheckedFood> result = new ArrayList<>();

   public void addResult(CheckedFood checkedFood) {
      result.add(checkedFood);
   }
}

