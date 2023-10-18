package allercheck.backend.global.web.resolver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // annotation이 생설될 수 있는 위치 지정(Parameter 선언시)
@Retention(RetentionPolicy.RUNTIME) // annotation이 언제까지 유효하는지 지정(Complie 이후에도 존재)
public @interface AuthMember {
}
