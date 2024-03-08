
package co.kr.compig.common.validator.annotaion;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import co.kr.compig.common.validator.CheckHscode;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CheckHscode.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidPhoneNum {
    String message() default "전화번호가 유효하지 않습니다. 번호만 입력해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
