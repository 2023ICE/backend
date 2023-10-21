package allercheck.backend.domain.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberSignInRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
