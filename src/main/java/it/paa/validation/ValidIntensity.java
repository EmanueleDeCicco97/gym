package it.paa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Indica che questa annotazione dovrebbe essere inclusa nella documentazione generata automaticamente.
@Documented
// Specifica la classe che implementa la logica di validazione per questa annotazione.
@Constraint(validatedBy = IntensityValidatorCheck.class)
// Indica che questa annotazione può essere applicata a qualsiasi tipo di elemento.
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
//Indica che questa annotazione deve essere conservata e resa disponibile in fase di runtime, in modo che
//possa essere utilizzata per la convalida dei dati a runtime.
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidIntensity {
    String message() default "Invalid intensity value. Must be 'easy', 'medium', or 'hard'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
