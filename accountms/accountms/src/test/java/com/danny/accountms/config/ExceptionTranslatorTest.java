package com.danny.accountms.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.exception.ConflictException;
import com.danny.accountms.exception.NotFoundException;
import com.danny.accountms.exception.UnAuthorizedException;
import com.danny.accountms.model.Error;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionTranslatorTest {

  private final ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

  @Test
  void testBadPetitionException() {
    BadPetitionException exception = new BadPetitionException("Bad request");
    ResponseEntity<Error> response = exceptionTranslator.badPetitionException(exception);

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Bad request", Objects.requireNonNull(response.getBody()).getMessage());
    assertEquals(400, response.getBody().getCode());
  }

  @Test
  void testNotFoundException() {
    NotFoundException exception = new NotFoundException("Resource not found");
    ResponseEntity<Error> response = exceptionTranslator.userNotFoundException(exception);

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
    assertEquals("Resource not found", Objects.requireNonNull(response.getBody()).getMessage());
    assertEquals(404, response.getBody().getCode());
  }

  @Test
  void testUnauthorizedException() {
    UnAuthorizedException exception = new UnAuthorizedException("Unauthorized access");
    ResponseEntity<Error> response = exceptionTranslator.unauthorizedException(exception);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
    assertEquals("Unauthorized access", Objects.requireNonNull(response.getBody()).getMessage());
    assertEquals(401, response.getBody().getCode());
  }

  @Test
  void testConflictException() {
    ConflictException exception = new ConflictException("Conflict occurred");
    ResponseEntity<Error> response = exceptionTranslator.unprocessableException(exception);

    assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode().value());
    assertEquals("Conflict occurred", Objects.requireNonNull(response.getBody()).getMessage());
    assertEquals(409, response.getBody().getCode());
  }
}
