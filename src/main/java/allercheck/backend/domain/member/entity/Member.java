package allercheck.backend.domain.member.entity;

import allercheck.backend.domain.allergy.entity.AllergyType;
import allercheck.backend.domain.allergy.application.AllergyTypeSetConverter;
import allercheck.backend.domain.auth.exception.InvalidNameFormatException;
import allercheck.backend.domain.auth.exception.InvalidPasswordFormatException;
import allercheck.backend.domain.auth.exception.InvalidUsernameFormatException;
import allercheck.backend.domain.auth.exception.NewPasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.PasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.PresentPasswordNotEqualsException;
import allercheck.backend.domain.auth.exception.UsernameNotEqualsException;
import allercheck.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    @Convert(converter = AllergyTypeSetConverter.class)
    private EnumSet<AllergyType> allergies;

    private Member(final String username, final String password, final String name) {
        validateMember(username, password, name);
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public static Member createMember(final String username, final String password, final String name) {
        return new Member(username, password, name);
    }

    public void changePassword(final String inputPassword) {
        this.password = inputPassword;
    }

    public void validateSignInInfo(final String inputUsername, final String inputPassword) {
        if(!this.username.equals(inputUsername)) {
            throw new UsernameNotEqualsException();
        }

        if(!this.password.equals(inputPassword)) {
            throw new PasswordNotEqualsException();
        }
    }

    public boolean validatePassword(final String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public static void validateMember(final String username, final String password, final String name) {
        validateUsernameFormat(username);
        validatePasswordFormat(password);
        validateNameFormat(name);
    }

    private static void validateUsernameFormat(final String username) {
        if(username == null || !username.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidUsernameFormatException();
        }
    }

    private static void validatePasswordFormat(final String password) {
        if(password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new InvalidPasswordFormatException();
        }
    }

    private static void validateNameFormat(final String name) {
        if (name == null || !(name.length() >= 2 && name.length() <= 4)) {
            throw new InvalidNameFormatException();
        }
    }

    public void changeAllergies(EnumSet<AllergyType> allergies) {
        this.allergies = allergies;
    }

    public void changePassword(final String presentPassword, final String newPassword, final String checkedNewPassword) {
        validatePassword(presentPassword, newPassword, checkedNewPassword);
        this.password = newPassword;
    }

    private void validatePassword(final String presentPassword, final String newPassword, final String checkedNewPassword) {
        if(!this.password.equals(presentPassword)) {
            throw new PresentPasswordNotEqualsException();
        }

        if(!newPassword.equals(checkedNewPassword)) {
            throw new NewPasswordNotEqualsException();
        }

        if(password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new InvalidPasswordFormatException();
        }
    }
}
