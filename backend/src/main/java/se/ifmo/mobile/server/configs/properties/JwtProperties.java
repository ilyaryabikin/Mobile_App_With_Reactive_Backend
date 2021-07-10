package se.ifmo.mobile.server.configs.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "application.jwt")
@ConstructorBinding
@Data
@RequiredArgsConstructor
public class JwtProperties {
  private final String authoritiesClaim;
  private final String secret;
  private final int expirationHours;
}
