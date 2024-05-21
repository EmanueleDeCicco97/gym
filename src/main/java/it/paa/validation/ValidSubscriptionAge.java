package it.paa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SubscriptionAgeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSubscriptionAge {
    String message() default "The age of the customer is not consistent with the type of subscription chosen";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
