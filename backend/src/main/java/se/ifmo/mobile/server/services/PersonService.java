package se.ifmo.mobile.server.services;

import static se.ifmo.mobile.server.exceptions.ExceptionFactory.mapToUserAlreadyExistsException;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.mapToValidationFailedException;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.userNotFoundByIdException;

import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.PersonDto;
import se.ifmo.mobile.server.exceptions.IllegalActionException;
import se.ifmo.mobile.server.mappers.PersonMapper;
import se.ifmo.mobile.server.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PersonService {

  private final UserRepository userRepository;
  private final PersonMapper personMapper;

  @Transactional(readOnly = true)
  public Flux<PersonDto> findAll(final Pageable pageable) {
    log.debug("Finding all users with pageable = {}", pageable);
    return userRepository.findAllByIdNotNull(pageable).map(personMapper::mapToDto);
  }

  @Transactional(readOnly = true)
  public Mono<User> findEntityById(final String id) {
    log.debug("Finding user with id = {}", id);
    return userRepository.findById(id).switchIfEmpty(Mono.error(userNotFoundByIdException(id)));
  }

  @Transactional(readOnly = true)
  public Mono<PersonDto> findDtoById(final String id) {
    log.debug("Finding user with id = {}", id);
    return userRepository
        .findById(id)
        .switchIfEmpty(Mono.error(userNotFoundByIdException(id)))
        .map(personMapper::mapToDto);
  }

  @Transactional
  public Mono<PersonDto> save(final PersonDto personDto) {
    log.debug("Saving user with username = {}", personDto.getUsername());
    final var person = personMapper.mapToPersistable(personDto);
    return userRepository
        .save(person)
        .onErrorMap(ConstraintViolationException.class, mapToValidationFailedException())
        .onErrorMap(
            DuplicateKeyException.class, mapToUserAlreadyExistsException(person.getUsername()))
        .map(personMapper::mapToDto);
  }

  @Transactional
  public Mono<PersonDto> update(final String id, final PersonDto personDto) {
    log.debug("Updating user with id = {} with personDto = {}", id, personDto);
    if (personDto.getUsername() != null) {
      return Mono.error(new IllegalActionException("Can not update username property"));
    } else if (personDto.getPassword() != null) {
      return Mono.error(new IllegalActionException("Can not update password property"));
    }

    final var person = findEntityById(id);
    return person
        .doOnNext(p -> personMapper.update(p, personDto))
        .flatMap(userRepository::save)
        .map(personMapper::mapToDto);
  }

  @Transactional
  public Mono<Void> delete(final String id) {
    log.debug("Deleting user with id = {}", id);
    return userRepository
        .findById(id)
        .switchIfEmpty(Mono.error(userNotFoundByIdException(id)))
        .flatMap(userRepository::delete);
  }
}
