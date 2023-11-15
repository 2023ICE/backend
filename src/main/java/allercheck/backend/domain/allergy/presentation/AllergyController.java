package allercheck.backend.domain.allergy.presentation;

import allercheck.backend.domain.allergy.application.AllergyService;
import allercheck.backend.domain.allergy.application.AllergyType;
import allercheck.backend.domain.allergy.application.dto.ChangeAllergiesRequest;
import allercheck.backend.domain.allergy.presentation.dto.CheckAllergyResponse;
import allercheck.backend.domain.allergy.presentation.dto.GetMyAllergiesResponse;
import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.global.web.resolver.annotation.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

@RequestMapping("/api/allergy")
@RequiredArgsConstructor
@Controller
public class AllergyController {

    private final AllergyService allergyService;

    @GetMapping("/check")
    public ResponseEntity<CheckAllergyResponse> checkAllergy(@AuthMember Member member, int page, String recipeName) {
        CheckAllergyResponse response = allergyService.checkAllergy(member, page, recipeName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<GetMyAllergiesResponse> getMyAllergies(@AuthMember Member member) {
        GetMyAllergiesResponse response = allergyService.getMyAllergies(member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping
    public ResponseEntity<GetMyAllergiesResponse> changeAllergies(@AuthMember Member member, @RequestBody ChangeAllergiesRequest request) {
        GetMyAllergiesResponse response = allergyService.changeAllergies(member, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}