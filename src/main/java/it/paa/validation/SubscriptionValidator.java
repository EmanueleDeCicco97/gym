package it.paa.validation;

import it.paa.model.Customer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class SubscriptionValidator implements ConstraintValidator<ValidSubscription, Customer> {

    private final List<String> allowedSubscriptions = Arrays.asList("daily", "monthly", "yearly");

    @Override
    public boolean isValid(Customer customer, ConstraintValidatorContext context) {
        // inizializzo la variabile 'valid' a true. sarà usata per tracciare la validità del cliente.
        boolean valid = true;
        // ottengo l'abbonamento attivo del cliente.
        String subscription = customer.getActiveSubscription();
        // ottengo la data di nascita del cliente.
        LocalDate birthDate = customer.getDateOfBirth();

        // se il cliente non ha un abbonamento attivo, ritorno true (valido di default).
        if (subscription == null) {
            return valid;
        }

        if (!allowedSubscriptions.contains(subscription)) {
            valid = false;
            // disabilito la validazione predefinita.
            context.disableDefaultConstraintViolation();
            // creo una nuova violazione di validazione.
            context.buildConstraintViolationWithTemplate(
                            "Subscription must be one of the following: daily, monthly, yearly")
                    .addPropertyNode("activeSubscription")
                    .addConstraintViolation();
        }
        // se la data di nascita non è null, controllo l'età del cliente.
        if (birthDate != null) {
            // ottengo l'età del cliente.'
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            // controllo se l'età del cliente è minore di 14 o maggiore di 65.'
            if (age < 14) {
                valid = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Customers must be at least 14 years old to subscribe")
                        .addPropertyNode("dateOfBirth")
                        .addConstraintViolation();
                // se l'abbonamento è annuale e l'età è superiore a 65 anni, imposto 'valid' a false e costruisco un messaggio di violazione
                // della costrizione.
            } else if ("yearly".equals(subscription) && age > 65) {
                valid = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Customers older than 65 cannot have a yearly subscription")
                        .addPropertyNode("activeSubscription")
                        .addConstraintViolation();
            }
        }
        // ritorno il valore di validità del cliente.
        return valid;
    }
}
