package ru.practicum.shareit.core.exception;

public class StatusException extends RuntimeException {
    public StatusException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }
}
