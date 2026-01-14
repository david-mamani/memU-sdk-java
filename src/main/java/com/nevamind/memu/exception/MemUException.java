package com.nevamind.memu.exception;

public class MemUException extends RuntimeException {

  public MemUException(String message) {
    super(message);
  }

  public MemUException(String message, Throwable cause) {
    super(message, cause);
  }
}
