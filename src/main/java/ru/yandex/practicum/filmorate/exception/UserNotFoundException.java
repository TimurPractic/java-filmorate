package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    /**
     * Exception for error validation.
     *@param message - message for this error.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
