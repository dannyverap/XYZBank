package com.danny.customerms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnAuthorizedException extends RuntimeException {

  private final int statusCode;

  public UnAuthorizedException(String message) {
    super(message);
    this.statusCode = HttpStatus.UNAUTHORIZED.value();
  }
}
