package it.paa.validation;

import it.paa.model.Customer;
import it.paa.model.TrainingProgram;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class TrainingIntensityValidator implements ConstraintValidator<ValidTrainingIntensity, TrainingProgram> {
    @Override
    public void initialize(ValidTrainingIntensity constraintAnnotation) {
    }

    @Override
    public boolean isValid(TrainingProgram trainingProgram, ConstraintValidatorContext context) {
        if (trainingProgram == null) {
            return true; // Consider null values as valid
        }

        Customer customer = trainingProgram.getAssociatedCustomer();
        if (customer == null) {
            return true; // This should be validated separately
        }

        LocalDate birthDate = customer.getDateOfBirth();
        if (birthDate == null) {
            return true; // This should be validated by @NotNull
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        String intensity = trainingProgram.getIntensity();

        // Logica di validazione, es: controllare se l'intensità è appropriata per l'età del cliente
        if (intensity != null) {
            if (age < 18 && intensity.equalsIgnoreCase("alta")) {
                return false; // L'alta intensità non è appropriata per i minorenni
            } else if (age >= 60 && intensity.equalsIgnoreCase("alta")) {
                return false; // L'alta intensità non è appropriata per gli anziani
            }
        }

        return true;
    }
}
