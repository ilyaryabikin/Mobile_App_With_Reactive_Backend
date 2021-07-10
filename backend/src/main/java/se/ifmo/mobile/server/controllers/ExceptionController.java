package se.ifmo.mobile.server.controllers;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.EXCEPTION;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.STACK_TRACE;
import static org.springframework.boot.web.error.ErrorAttributeOptions.defaults;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import se.ifmo.mobile.server.exceptions.EntityAlreadyExistsException;
import se.ifmo.mobile.server.exceptions.EntityNotFoundException;
import se.ifmo.mobile.server.exceptions.ForbiddenActionException;
import se.ifmo.mobile.server.exceptions.IllegalActionException;
import se.ifmo.mobile.server.exceptions.UnauthorizedException;
import se.ifmo.mobile.server.exceptions.ValidationFailedException;

@Component
@Order(-2)
@Slf4j
public class ExceptionController extends AbstractErrorWebExceptionHandler {

  @Autowired
  public ExceptionController(
      final DefaultErrorAttributes errorAttributes,
      final WebProperties.Resources resources,
      final ApplicationContext applicationContext,
      final ServerCodecConfigurer serverCodecConfigurer) {
    super(errorAttributes, resources, applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(
      final ErrorAttributes errorAttributes) {
    return route(
        all(),
        request -> {
          final var exception = errorAttributes.getError(request);
          final var attributes =
              errorAttributes.getErrorAttributes(
                  request, defaults().excluding(STACK_TRACE, EXCEPTION));

          log.warn(
              "Caught exception with error={} during request with id={}",
              exception.getMessage().replace("\n", " "),
              attributes.get("requestId"));

          var status = I_AM_A_TEAPOT;
          String message;
          if (exception instanceof ResponseStatusException) {
            if (exception instanceof WebExchangeBindException e) {
              final var errorMessageBuilder =
                  new StringBuilder("Failed field validation with messages: ");
              for (final var fieldError : e.getFieldErrors()) {
                errorMessageBuilder
                    .append("wrong value for field ")
                    .append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
              }
              errorMessageBuilder.delete(
                  errorMessageBuilder.lastIndexOf("; "), errorMessageBuilder.length());
              message = errorMessageBuilder.toString();
              status = BAD_REQUEST;
            } else {
              status = ((ResponseStatusException) exception).getStatus();
              message = exception.getMessage();
            }
          } else if (exception instanceof ValidationFailedException
              || exception instanceof ConversionFailedException
              || exception instanceof IllegalActionException) {
            message = exception.getMessage();
            status = BAD_REQUEST;
          } else if (exception instanceof UnauthorizedException) {
            message = exception.getMessage();
            status = UNAUTHORIZED;
          } else if (exception instanceof ForbiddenActionException) {
            message = exception.getMessage();
            status = FORBIDDEN;
          } else if (exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = NOT_FOUND;
          } else if (exception instanceof EntityAlreadyExistsException) {
            message = exception.getMessage();
            status = CONFLICT;
          } else {
            message = exception.getMessage();
            status = INTERNAL_SERVER_ERROR;
          }
          attributes.put("status", status.value());
          attributes.put("error", status.getReasonPhrase());
          attributes.put("message", message);

          return ServerResponse.status(status)
              .contentType(APPLICATION_JSON)
              .body(fromValue(attributes));
        });
  }
}
