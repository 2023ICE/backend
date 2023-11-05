package allercheck.backend.global.error;

import allercheck.backend.domain.auth.exception.*;
import allercheck.backend.domain.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> invalidTokenException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("유효하지 않은 토큰입니다");
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<?> loginFailureException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("email, password를 다시 확인해주세요.");
    }


    @ExceptionHandler(PasswordAndCheckedPasswordNotEqualsException.class)
    public ResponseEntity<?> passwordAndCheckedPasswordNotEqualsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("확인용 비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(InvalidUsernameFormatException.class)
    public ResponseEntity<?> invalidUsernameFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("email 형식에 맞춰 입력해주세요.");
    }

    @ExceptionHandler(InvalidPasswordFormatException.class)
    public ResponseEntity<?> invalidPasswordFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("특수문자, 알파벳, 숫자를 포함하여 8자 이상 입력해주세요.");
    }

    @ExceptionHandler(InvalidNameFormatException.class)
    public ResponseEntity<?> invalidNameFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("이름의 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<?> tokenNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Access Token이 존재하지 않습니다");
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> usernameAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("이미 회원가입된 email입니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> memberNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("해당 회원을 찾을 수 없습니다.");
    }

    @ExceptionHandler(UsernameNotEqualsException.class)
    public ResponseEntity<?> usernameNotEqualsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("email 정보가 일치하지 않습니다.");
    }

    @ExceptionHandler(PasswordNotEqualsException.class)
    public ResponseEntity<?> passwordNotEqualsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("올바른 password를 입력해 주세요.");
    }

    @ExceptionHandler(PresentPasswordNotEqualsException.class)
    public ResponseEntity<?> presentPasswordNotEqualsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("현재 password를 올바르게 입력해주세요.");
    }

    @ExceptionHandler(NewPasswordNotEqualsException.class)
    public ResponseEntity<?> newPasswordNotEqualsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("새로운 password를 올바르게 입력해주세요.");
    }
}
