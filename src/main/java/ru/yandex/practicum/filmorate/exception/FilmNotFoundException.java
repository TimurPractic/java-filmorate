package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    /**
     * Exception for error validation.
     *@param message - message for this error.
     */
    public FilmNotFoundException(String message) {
        super(message);
    }
}
