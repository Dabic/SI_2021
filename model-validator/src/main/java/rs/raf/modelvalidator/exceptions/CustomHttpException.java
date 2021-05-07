package rs.raf.modelvalidator.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomHttpException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public CustomHttpException(String message, HttpStatus status) {

        this.message = message;
        this.status = status;
    }
}
