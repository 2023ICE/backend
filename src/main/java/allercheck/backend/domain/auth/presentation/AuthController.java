package allercheck.backend.domain.auth.presentation;

import allercheck.backend.domain.auth.application.dto.MemberSignInRequest;
import allercheck.backend.domain.auth.presentation.dto.MemberResponse;
import allercheck.backend.domain.auth.application.AuthService;
import allercheck.backend.domain.auth.application.dto.MemberSignUpRequest;
import allercheck.backend.domain.auth.presentation.dto.SignInResponse;
import allercheck.backend.domain.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody final MemberSignUpRequest memberSignUpRequest) {
        Member member = authService.signUp(memberSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MemberResponse.toDto(member));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody final MemberSignInRequest memberSignInRequest) {
        Pair<String, String> nameAndAccessToken = authService.signIn(memberSignInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignInResponse(nameAndAccessToken.getFirst(), nameAndAccessToken.getSecond()));
    }
}
