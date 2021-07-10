package se.ifmo.mobile.server.services;

import static se.ifmo.mobile.server.exceptions.ExceptionFactory.mapToUserAlreadyExistsException;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.mapToValidationFailedException;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.userNotFoundByUsernameException;

import com.mongodb.DuplicateKeyException;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.UserDto;
import se.ifmo.mobile.server.mappers.UserMapper;
import se.ifmo.mobile.server.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  public Mono<User> findEntityByUsername(final String username) {
    log.debug("Finding user with username = {}", username);
    return userRepository
        .findByUsername(username)
        .switchIfEmpty(Mono.error(userNotFoundByUsernameException(username)));
  }

  @Transactional
  public Mono<UserDto> saveByDto(final UserDto userDto) {
    log.debug("Saving new user with username = {}", userDto.getUsername());
    final var user = userMapper.mapToPersistable(userDto);
    return userRepository
        .save(user)
        .onErrorMap(ConstraintViolationException.class, mapToValidationFailedException())
        .onErrorMap(
            DuplicateKeyException.class, mapToUserAlreadyExistsException(user.getUsername()))
        .map(userMapper::mapToDto);
  }

  @Transactional
  public Mono<User> save(final User user) {
    log.debug("Saving new user with username = {}", user.getUsername());
    return userRepository
        .save(user)
        .onErrorMap(ConstraintViolationException.class, mapToValidationFailedException())
        .onErrorMap(
            DuplicateKeyException.class, mapToUserAlreadyExistsException(user.getUsername()));
  }
}
