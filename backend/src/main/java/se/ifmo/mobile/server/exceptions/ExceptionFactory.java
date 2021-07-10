package se.ifmo.mobile.server.exceptions;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

import java.util.function.Function;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ExceptionFactory {

  public static EntityNotFoundException userNotFoundByIdException(final String id) {
    return new EntityNotFoundException(format("User with id %s was not found", id));
  }

  public static EntityNotFoundException userNotFoundByUsernameException(final String username) {
    return new EntityNotFoundException(format("User with username %s was not found", username));
  }

  public static UnauthorizedException defaultUnauthorizedException() {
    return new UnauthorizedException("Bad credentials");
  }

  public static Function<? super Throwable, EntityAlreadyExistsException>
      mapToEntityAlreadyExistsException(
          final String entity, final String propertyName, final String propertyValue) {
    return error ->
        new EntityAlreadyExistsException(
            format("%s with %s %s already exists", entity, propertyName, propertyValue));
  }

  public static Function<? super Throwable, EntityAlreadyExistsException>
      mapToUserAlreadyExistsException(final String username) {
    return error ->
        new EntityAlreadyExistsException(format("User with username %s already exists", username));
  }

  public static Function<? super Throwable, EntityNotFoundException> mapToEntityNotFoundException(
      final String entity, final String propertyName, final String propertyValue) {
    return error ->
        new EntityNotFoundException(
            format("%s with %s %s was not found", entity, propertyName, propertyValue));
  }

  public static Function<? super Throwable, ValidationFailedException>
      mapToValidationFailedException() {
    return error -> new ValidationFailedException(format("%s", error.getMessage()));
  }

  public static Function<? super Throwable, UnauthorizedException>
      mapToDefaultUnauthorizedException() {
    return error -> new UnauthorizedException("Bad credentials");
  }
}
