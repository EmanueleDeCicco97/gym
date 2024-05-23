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
        boolean valid = true;
        String subscription = customer.getActiveSubscription();
        LocalDate birthDate = customer.getDateOfBirth();

        if (subscription == null) {
            return valid;
        }

        if (!allowedSubscriptions.contains(subscription)) {
            valid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Subscription must be one of the following: daily, monthly, yearly")
                    .addPropertyNode("activeSubscription")
                    .addConstraintViolation();
        }

        if (birthDate != null) {
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            if (age < 14) {
                valid = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Customers must be at least 14 years old to subscribe")
                        .addPropertyNode("dateOfBirth")
                        .addConstraintViolation();
            } else if ("yearly".equals(subscription) && age > 65) {
                valid = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Customers older than 65 cannot have a yearly subscription")
                        .addPropertyNode("activeSubscription")
                        .addConstraintViolation();
            }
        }

        return valid;
    }
}
