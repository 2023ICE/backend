package allercheck.backend.domain.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignInRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
