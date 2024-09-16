package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface for creation validation.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationValidator.class)
public @interface ValidDuration {
    /**
     * The message of the violation.
     */
    String message() default "Продолжительность должна быть положительным числом";
    /**
     * Standard element for group validation.
     */
    Class<?>[] groups() default {};
    /**
     * Standard element for keeping validation metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
