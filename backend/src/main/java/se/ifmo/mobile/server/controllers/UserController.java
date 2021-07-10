package se.ifmo.mobile.server.controllers;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.RestErrorDto;
import se.ifmo.mobile.server.dtos.UserDto;
import se.ifmo.mobile.server.mappers.UserMapper;
import se.ifmo.mobile.server.services.UserService;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping(
      path = "/current",
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(OK)
  @Operation(
      operationId = "getCurrentUser",
      summary = "Получение информации о текущем пользователе по Bearer-токену",
      tags = {"users", "current"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Запрос обработан успешно",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Отсутствует Bearer-токен или он не валиден",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден в базе",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "Сервер не выдает ответ в желаемом формате",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Mono<UserDto> getCurrentUser(final Authentication authentication) {
    final var principal = (User) authentication.getPrincipal();
    return Mono.just(principal).map(userMapper::mapToDto);
  }
}
