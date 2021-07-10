package se.ifmo.mobile.server.exceptions;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(final String message) {
    super(message);
  }
}
