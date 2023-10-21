package allercheck.backend.domain.member.domain;

import allercheck.backend.domain.auth.exception.LoginFailureException;
import allercheck.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    public Member(final String username, final String password, final String name) {
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
            throw new LoginFailureException();
        }

        if(!this.password.equals(inputPassword)) {
            throw new LoginFailureException();
        }
    }

    public boolean validatePassword(final String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
