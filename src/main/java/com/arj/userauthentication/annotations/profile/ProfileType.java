package com.arj.userauthentication.annotations.profile;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = { ProfileTypeValidator.class })
public @interface ProfileType {
  String message();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
