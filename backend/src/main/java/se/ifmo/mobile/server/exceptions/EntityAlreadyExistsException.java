package se.ifmo.mobile.server.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

  public EntityAlreadyExistsException(final String message) {
    super(message);
  }
}
