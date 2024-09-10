package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    /**
     * Exception for error validation.
     *@param message - message for this error.
     */
    public ValidationException(String message) {
        super(message);
    }
}
