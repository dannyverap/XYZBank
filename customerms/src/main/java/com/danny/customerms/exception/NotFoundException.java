package com.danny.customerms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {

  private final int statusCode;

  public NotFoundException(String message) {
    super(message);
    this.statusCode = HttpStatus.NOT_FOUND.value();
  }
}
