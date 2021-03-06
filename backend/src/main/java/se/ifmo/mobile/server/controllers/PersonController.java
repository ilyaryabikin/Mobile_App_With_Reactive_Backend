package se.ifmo.mobile.server.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.PersonDto;
import se.ifmo.mobile.server.dtos.RestErrorDto;
import se.ifmo.mobile.server.mappers.PersonMapper;
import se.ifmo.mobile.server.services.PersonService;

@RestController
@RequestMapping(path = "/api/persons")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonController {

  private final PersonService personService;
  private final PersonMapper personMapper;

  @PostMapping(
      consumes = APPLICATION_JSON_VALUE,
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("permitAll()")
  @ResponseStatus(CREATED)
  @Operation(
      operationId = "saveNewPerson",
      summary = "???????????????? ???????????? ????????????????????????",
      tags = {"persons"},
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "???????????? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = PersonDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "?????????????????? ?????????????? ?????? ?????????????????? ???????? ?????????????? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "409",
            description = "???????????????????????? ?? ?????????? ???????????? ?????? ????????????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "415",
            description = "???????????? ???? ?????????????????? ?????????????? ?? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  public Mono<PersonDto> saveNewPerson(final @Valid @RequestBody PersonDto personDto) {
    return personService.save(personDto);
  }

  @PatchMapping(
      path = "/{id}",
      consumes = APPLICATION_JSON_VALUE,
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize(
      "(isAuthenticated() and #id == authentication.principal.id.toString()) or hasRole('ROLE_ADMIN')")
  @ResponseStatus(OK)
  @Operation(
      operationId = "updatePersonById",
      summary = "???????????????????? ?????????????????? ?????????? ???????????????????? ????????????????????????",
      description =
          "?????? ???????????????????? ???????????????? ?????????? ???????????????? ??????????????: ???????? ???????? ??????????????????????????, ???????????????????? "
              + "?? ?????????????? ????????????????????, ???????? ?????????? ?????????? ????????????????????????????. ???????? username ?? password ???????????????? ????????????",
      tags = {"persons"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "???????????? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = PersonDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "?????????????????? ?????????????? ?????? ?????????????????? ???????? ?????????????? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "?????????????????????? Bearer-?????????? ?????? ???? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "403",
            description = "???????????????????????? ???????? ?????? ???????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ???? ???????????? ?? ????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "415",
            description = "???????????? ???? ?????????????????? ?????????????? ?? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Mono<PersonDto> updatePersonById(
      final @PathVariable String id, final @Valid @RequestBody PersonDto personDto) {
    return personService.update(id, personDto);
  }

  @DeleteMapping(path = "/{id}")
  @PreAuthorize(
      "(isAuthenticated() and #id == authentication.principal.id.toString()) or hasRole('ROLE_ADMIN')")
  @ResponseStatus(NO_CONTENT)
  @Operation(
      operationId = "deletePersonById",
      summary = "???????????????? ???????????????????????? ???? ?????? ????????????????????????????",
      description =
          "?????? ???????????????????? ???????????????? ?????????? ???????????????? ??????????????: ???????? ???????? ??????????????????????????, ???????????????????? "
              + "?? ?????????????? ????????????????????, ???????? ?????????? ?????????? ????????????????????????????. ???????? username ?? password ???????????????? ????????????",
      tags = {"persons"},
      responses = {
        @ApiResponse(responseCode = "204", description = "???????????? ?????????????????? ??????????????"),
        @ApiResponse(
            responseCode = "400",
            description = "?????????????????? ?????????????? ?????? ?????????????????? ???????? ?????????????? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "?????????????????????? Bearer-?????????? ?????? ???? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "403",
            description = "???????????????????????? ???????? ?????? ???????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ???? ???????????? ?? ????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "415",
            description = "???????????? ???? ?????????????????? ?????????????? ?? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Mono<Void> deletePersonById(final @PathVariable String id) {
    return personService.delete(id);
  }

  @GetMapping(
      path = "/{id}",
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(OK)
  @Operation(
      operationId = "getPersonById",
      summary = "?????????????????? ???????????????????? ?? ???????????????????????? ?? ?????????????????? ??????????????????????????????",
      tags = {"persons"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "???????????? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = PersonDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "?????????????????? ?????????????? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "?????????????????????? Bearer-?????????? ?????? ???? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ?? ?????????????????? ?????????????????????????????? ???? ????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ???? ???????????? ?? ????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Mono<PersonDto> getPersonById(final @PathVariable String id) {
    return personService.findDtoById(id);
  }

  @GetMapping(produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(OK)
  @Operation(
      operationId = "getAllPersons",
      summary = "?????????????????? ???????????????????? ?????? ???????? ?????????????????????????? ?? ??????????????",
      tags = {"persons"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "???????????? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = PersonDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "?????????????????? ?????????????? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "?????????????????????? Bearer-?????????? ?????? ???? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ???? ???????????? ?? ????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Flux<PersonDto> getAllPersons(final @RequestParam(required = false) Pageable pageable) {
    return personService.findAll(pageable);
  }

  @GetMapping(
      path = "/current",
      produces = {APPLICATION_JSON_VALUE, TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(OK)
  @Operation(
      operationId = "getCurrentPerson",
      summary = "?????????????????? ???????????????????? ?? ?????????????? ???????????????????????? ???? Bearer ????????????",
      tags = {"persons", "current"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "???????????? ?????????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = PersonDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "?????????????????????? Bearer-?????????? ?????? ???? ???? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "???????????????????????? ???? ???????????? ?? ????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class))),
        @ApiResponse(
            responseCode = "406",
            description = "???????????? ???? ???????????? ?????????? ?? ???????????????? ??????????????",
            content = @Content(schema = @Schema(implementation = RestErrorDto.class)))
      })
  @SecurityRequirement(name = "bearer-jwt")
  public Mono<PersonDto> getCurrentPerson(final Authentication authentication) {
    final var principal = (User) authentication.getPrincipal();
    return Mono.just(principal).map(personMapper::mapToDto);
  }
}
