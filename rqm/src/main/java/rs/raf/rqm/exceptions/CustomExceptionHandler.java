package rs.raf.rqm.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<?> handleException(CustomHttpException e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(e.getMessage());
    }
}
