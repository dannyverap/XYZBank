package com.danny.customerms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadPetitionException extends RuntimeException {

  private final int statusCode;

  public BadPetitionException(String message) {
    super(message);
    this.statusCode = HttpStatus.BAD_REQUEST.value();
  }
}
