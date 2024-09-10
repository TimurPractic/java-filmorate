package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationValidator.class)
public @interface ValidDuration {
    String message() default "Продолжительность должна быть положительным числом";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
