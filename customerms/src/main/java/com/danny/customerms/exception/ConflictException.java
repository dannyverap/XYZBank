package com.danny.customerms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConflictException extends RuntimeException {

  private final int statusCode;

  public ConflictException(String message) {
    super(message);
    this.statusCode = HttpStatus.CONFLICT.value();
  }
}
