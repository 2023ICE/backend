package allercheck.backend.domain.auth.presentation;

import allercheck.backend.domain.auth.presentation.dto.MemberResponse;
import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody final MemberSignUpRequest memberSignUpRequest) {
        return ResponseEntity.ok(authService.signUp(memberSignUpRequest));
    }
}
