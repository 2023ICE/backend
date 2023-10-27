package allercheck.backend.domain.auth.presentation;

import allercheck.backend.domain.auth.application.dto.MemberSignInRequest;
import allercheck.backend.domain.auth.presentation.dto.MemberResponse;
import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.auth.presentation.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody final MemberSignUpRequest memberSignUpRequest) {
        return ResponseEntity.ok(authService.signUp(memberSignUpRequest));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@Valid @RequestBody final MemberSignInRequest memberSignInRequest) {
        return ResponseEntity.ok(authService.signIn(memberSignInRequest));
    }
}
