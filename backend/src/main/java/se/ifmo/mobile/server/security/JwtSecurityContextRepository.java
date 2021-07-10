package se.ifmo.mobile.server.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static reactor.core.publisher.Mono.empty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

  private final JwtAuthenticationManager jwtAuthenticationManager;

  @Override
  public Mono<Void> save(final ServerWebExchange exchange, final SecurityContext context) {
    log.error("Called unsupported save method");
    throw new UnsupportedOperationException("Saving security context is not supported");
  }

  @Override
  public Mono<SecurityContext> load(final ServerWebExchange exchange) {
    final var serverHttpRequest = exchange.getRequest();
    final var authHeader = serverHttpRequest.getHeaders().getFirst(AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      final var authToken = authHeader.substring(7);
      final var jwtAuth = new UsernamePasswordAuthenticationToken(authToken, authToken);
      return jwtAuthenticationManager.authenticate(jwtAuth).map(SecurityContextImpl::new);
    } else {
      return empty();
    }
  }
}
