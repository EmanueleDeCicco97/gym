package it.paa.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class IntensityValidatorCheck implements ConstraintValidator<ValidIntensity, String> {

    private final List<String> allowedIntensities = Arrays.asList("easy", "medium", "hard");

    @Override
    public void initialize(ValidIntensity constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return allowedIntensities.contains(value.toLowerCase());
    }
}
