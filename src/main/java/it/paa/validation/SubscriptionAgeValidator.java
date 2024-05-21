package it.paa.validation;

import it.paa.model.Customer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class SubscriptionAgeValidator implements ConstraintValidator<ValidSubscriptionAge, Customer> {
    @Override
    public void initialize(ValidSubscriptionAge constraintAnnotation) {
    }

    @Override
    public boolean isValid(Customer customer, ConstraintValidatorContext context) {
        if (customer == null) {
            return true;
        }

        LocalDate birthDate = customer.getDateOfBirth();
        if (birthDate == null) {
            return true;
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        Boolean hasSubscription = customer.getActiveSubscription();

        //  controllo se l'età è appropriata per il tipo di abbonamento
        if (hasSubscription) {
            // supponiamo che il tipo di abbonamento richieda un'età minima di 14 anni
            return age >= 14;
        }

        return true;
    }
}
