package se.ifmo.mobile.server.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.defaultUnauthorizedException;
import static se.ifmo.mobile.server.exceptions.ExceptionFactory.mapToDefaultUnauthorizedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.dtos.LoginDto;
import se.ifmo.mobile.server.dtos.RestErrorDto;
import se.ifmo.mobile.server.exceptions.EntityNotFoundException;
import se.ifmo.mobile.server.mappers.LoginMapper;
import se.ifmo.mobile.server.services.UserService;
import se.ifmo.mobile.server.utils.JwtUtil;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationController {

  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final LoginMapper loginMapper;
  private final UserService userService;

  @PostMapping(
      path = "/login",
      consumes = APPLICATION_JSON_VALUE,
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("permitAll()")
  @Operation(
      summary = "Авторизация пользователя по логину и паролю",
      tags = {"authorization"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Запрос обработан успешно",
            content = @Content(schema = @Schema(implementation = LoginDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса или параметры тела запроса не валидны",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Введенный логин или пароль не верен",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "Сервер не выдает ответ в желаемом формате",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "415",
            description = "Сервер не принимает запросы в указанном формате",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  public Mono<LoginDto> login(final @Valid @RequestBody LoginDto loginDto) {
    return userService
        .findEntityByUsername(loginDto.getUsername())
        .onErrorMap(EntityNotFoundException.class, mapToDefaultUnauthorizedException())
        .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
        .flatMap(
            user -> {
              final var token = jwtUtil.generateJwtToken(user);
              final var expiresAt = jwtUtil.getExpirationDate(token);
              return just(loginMapper.mapToDto(user, token, expiresAt));
            })
        .switchIfEmpty(error(defaultUnauthorizedException()));
  }
}
