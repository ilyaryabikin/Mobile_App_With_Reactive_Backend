package se.ifmo.mobile.server.exceptions;

public class ForbiddenActionException extends RuntimeException {

  public ForbiddenActionException(final String message) {
    super(message);
  }
}
