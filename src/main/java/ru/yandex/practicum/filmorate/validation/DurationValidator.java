package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class DurationValidator implements ConstraintValidator<ValidDuration, Duration> {
    /**
     * This method is for correct implementation from parent.
     */
    @Override
    public void initialize(ValidDuration constraintAnnotation) {
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        if (duration == null) {
            return true;
        }
        return !duration.isNegative();
    }
}
