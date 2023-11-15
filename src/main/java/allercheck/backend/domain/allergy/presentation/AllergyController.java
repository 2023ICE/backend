package allercheck.backend.domain.allergy.presentation;

import allercheck.backend.domain.allergy.application.AllergyService;
import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.presentation.dto.CheckAllergyResponse;
import allercheck.backend.domain.allergy.presentation.dto.MyAllergiesResponse;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.web.resolver.annotation.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/allergy")
@Controller
public class AllergyController {

    private final AllergyService allergyService;

    @GetMapping("/check")
    public ResponseEntity<CheckAllergyResponse> checkAllergyByRecipeName(@AuthMember Member member, int page, String recipeName) {
        CheckAllergyResponse response = allergyService.checkAllergyByRecipeName(member, page, recipeName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<MyAllergiesResponse> getMyAllergies(@AuthMember Member member) {
        MyAllergiesResponse response = allergyService.getMyAllergies(member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<MyAllergiesResponse> changeAllergies(@AuthMember Member member, @RequestBody ChangeAllergiesRequest request) {
        MyAllergiesResponse response = allergyService.changeAllergies(member, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
