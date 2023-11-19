package allercheck.backend.domain.member.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberPasswordEditRequest {

    @NotBlank
    private String presentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String checkedNewPassword;
}
