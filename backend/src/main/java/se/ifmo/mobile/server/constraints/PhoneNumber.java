package se.ifmo.mobile.server.constraints;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {

  String defaultRegion() default "RU";

  String message() default "Invalid phone number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
