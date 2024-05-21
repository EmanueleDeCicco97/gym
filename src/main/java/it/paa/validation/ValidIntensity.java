package it.paa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IntensityValidatorCheck.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIntensity {
    String message() default "Invalid intensity value. Must be 'easy', 'medium', or 'hard'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
