package com.danny.accountms.config;

import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.exception.ConflictException;
import com.danny.accountms.exception.NotFoundException;
import com.danny.accountms.exception.UnAuthorizedException;
import com.danny.accountms.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionTranslator {

  public ExceptionTranslator() {
  }

  @ExceptionHandler({BadPetitionException.class})
  public ResponseEntity<Error> badPetitionException(final BadPetitionException e) {
    Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
    return this.formErrorResponse(e.getStatusCode(), generatedError);
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<Error> userNotFoundException(final NotFoundException e) {
    Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
    return this.formErrorResponse(e.getStatusCode(), generatedError);
  }

  @ExceptionHandler({UnAuthorizedException.class})
  public ResponseEntity<Error> unauthorizedException(final UnAuthorizedException e) {
    Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
    return this.formErrorResponse(e.getStatusCode(), generatedError);
  }

  @ExceptionHandler({ConflictException.class})
  public ResponseEntity<Error> unprocessableException(final ConflictException e) {
    Error generatedError = this.createErrorModel(e.getMessage(), e.getStatusCode());
    return this.formErrorResponse(e.getStatusCode(), generatedError);
  }

  private Error createErrorModel(String message, Integer code) {
    Error error = new Error();
    error.setCode(code);
    error.setMessage(message);
    return error;
  }

  private ResponseEntity<Error> formErrorResponse(int code, Error error) {
    return ResponseEntity.status(code).body(error);
  }
}
