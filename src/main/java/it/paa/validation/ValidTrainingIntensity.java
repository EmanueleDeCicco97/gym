package it.paa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrainingIntensityValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTrainingIntensity {
    String message() default "L'intensità dell'allenamento non è appropriata per il cliente";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}