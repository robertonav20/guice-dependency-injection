package application.context.exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final Exception exception;
    private final String message;

    public ApplicationException(Exception exception) {
        this.exception = exception;
        this.message = exception.getMessage();
    }
}
