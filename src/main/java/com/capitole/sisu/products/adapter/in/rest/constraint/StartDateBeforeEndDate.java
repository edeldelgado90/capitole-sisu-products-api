package com.capitole.sisu.products.adapter.in.rest.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartDateBeforeEndDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateBeforeEndDate {
  String message() default "{price.startDate.beforeEndDate}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
