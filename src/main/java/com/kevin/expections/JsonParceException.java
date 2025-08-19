package com.kevin.expections;

public class JsonParceException extends RuntimeException {
  public JsonParceException(String message) {
    super(message);
  }

  public JsonParceException(String message, Throwable cause) {
    super(message, cause);
  }
}
