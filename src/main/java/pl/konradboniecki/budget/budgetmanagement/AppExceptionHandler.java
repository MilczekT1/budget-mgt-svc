package pl.konradboniecki.budget.budgetmanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarCreationException;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarNotFoundException;
import pl.konradboniecki.chassis.ErrorDescription;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDescription> jarNotFoundException(JarNotFoundException e){
        log.error(e.getMessage());
        final ErrorDescription errorDescription = new ErrorDescription(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity
                .status(errorDescription.getStatus())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(errorDescription);
    }

    @ExceptionHandler(JarCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDescription> jarCreationException(JarCreationException e){
        log.error(e.getMessage());
        final ErrorDescription errorDescription = new ErrorDescription(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity
                .status(errorDescription.getStatus())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(errorDescription);
    }
    //TODO: budget conflict exception
}
