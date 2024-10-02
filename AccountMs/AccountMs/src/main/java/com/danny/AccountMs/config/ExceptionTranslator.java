package com.danny.AccountMs.config;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.exception.ConflictException;
import com.danny.AccountMs.exception.NotFoundException;
import com.danny.AccountMs.exception.UnauthorizedException;
import com.danny.AccountMs.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionTranslator {
    public ExceptionTranslator() {
    }

    @ExceptionHandler({BadPetitionException.class})
    public ResponseEntity<Error> badPetitionException(final BadPetitionException e){
        Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
        return this.formErrorResponse(e.getStatusCode(), generatedError);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Error> userNotFoundException(final NotFoundException e){
        Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
        return this.formErrorResponse(e.getStatusCode(), generatedError);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Error> unauthorizedException(final UnauthorizedException e){
        Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
        return this.formErrorResponse(e.getStatusCode(), generatedError);
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<Error> unprocessableException(final ConflictException e){
        Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
        return this.formErrorResponse(e.getStatusCode(), generatedError);
    }

    private Error createErrorModel(String message, Integer code){
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }
    private ResponseEntity<Error> formErrorResponse(int code, Error error){
        return ResponseEntity.status(code).body(error);
    }
}
