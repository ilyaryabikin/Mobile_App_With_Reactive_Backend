package se.ifmo.mobile.server.utils;

import static io.jsonwebtoken.io.Decoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.time.temporal.ChronoUnit.HOURS;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import se.ifmo.mobile.server.configs.properties.JwtProperties;

@Component
@Slf4j
public final class JwtUtil {

  private final JwtProperties jwtProperties;
  private final Key secretKey;

  @Autowired
  public JwtUtil(final JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = hmacShaKeyFor(BASE64.decode(jwtProperties.getSecret()));
  }

  public String generateJwtToken(final UserDetails userDetails) {
    final var issueDate = Instant.now();
    final var expirationDate = issueDate.plus(jwtProperties.getExpirationHours(), HOURS);

    final var jwtToken =
        Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim(
                jwtProperties.getAuthoritiesClaim(),
                userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .setIssuedAt(Date.from(issueDate))
            .setExpiration(Date.from(expirationDate))
            .signWith(secretKey)
            .compact();

    log.info(
        "Generated JWT token for user={} with expirationDate={}",
        userDetails.getUsername(),
        expirationDate);

    return jwtToken;
  }

  public String generateJwtToken(final Authentication authentication) {
    final var user = (UserDetails) authentication.getPrincipal();
    return generateJwtToken(user);
  }

  public Claims getAllClaimsFromToken(final String jwtToken) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();
  }

  public Collection<GrantedAuthority> getAuthorities(final String token) {
    final var claims = getAllClaimsFromToken(token);
    return getAuthorities(claims);
  }

  @SuppressWarnings("unchecked")
  public Collection<GrantedAuthority> getAuthorities(final Claims claims) {
    final var authorities = (List<Object>) claims.get(jwtProperties.getAuthoritiesClaim());
    return authorities.stream()
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toUnmodifiableSet());
  }

  public String getUsername(final String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDate(final String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  public boolean isTokenExpired(final String token) {
    final var expirationTime = getExpirationDate(token);
    return expirationTime.before(new Date());
  }

  public boolean isTokenValid(final String token) {
    return !isTokenExpired(token);
  }
}
