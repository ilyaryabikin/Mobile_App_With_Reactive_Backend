package se.ifmo.mobile.server.exceptions;

public class ValidationFailedException extends RuntimeException {

  public ValidationFailedException(final String message) {
    super(message);
  }
}
