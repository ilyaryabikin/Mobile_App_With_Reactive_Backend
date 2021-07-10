package se.ifmo.mobile.server.exceptions;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(final String message) {
    super(message);
  }
}
