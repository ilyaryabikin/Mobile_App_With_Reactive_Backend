package se.ifmo.mobile.server.security;

import static reactor.core.publisher.Mono.error;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.ifmo.mobile.server.exceptions.EntityNotFoundException;
import se.ifmo.mobile.server.exceptions.UnauthorizedException;
import se.ifmo.mobile.server.services.UserService;
import se.ifmo.mobile.server.utils.JwtUtil;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Override
  public Mono<Authentication> authenticate(final Authentication authentication) {
    final var authToken = authentication.getCredentials().toString();
    log.info("Authenticating user with token {}", authToken);
    try {
      if (jwtUtil.isTokenExpired(authToken)) {
        log.warn("Authentication token is expired");
        return Mono.error(new UnauthorizedException("Authentication token is expired"));
      }
      final var subject = jwtUtil.getUsername(authToken);
      final var authorities = jwtUtil.getAuthorities(authToken);
      log.debug(
          "Authentication token identifies user {} with authorities {}", subject, authorities);

      return userService
          .findEntityByUsername(subject)
          .onErrorMap(EntityNotFoundException.class, e -> new UnauthorizedException(e.getMessage()))
          .map(user -> new UsernamePasswordAuthenticationToken(user, null, authorities));
    } catch (final JwtException e) {
      log.warn("Caught jwt exception {}", e.getMessage());
      return error(() -> new UnauthorizedException(e.getMessage()));
    }
  }
}
