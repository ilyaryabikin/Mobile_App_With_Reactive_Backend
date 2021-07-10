package se.ifmo.mobile.server.configs;

import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.$2B;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import se.ifmo.mobile.server.security.JwtAuthenticationManager;
import se.ifmo.mobile.server.security.JwtSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig {

  private final JwtAuthenticationManager jwtAuthenticationManager;
  private final JwtSecurityContextRepository jwtSecurityContextRepository;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
    return http.exceptionHandling()
        .and()
        .cors()
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .logout()
        .disable()
        .requestCache()
        .disable()
        .authenticationManager(jwtAuthenticationManager)
        .securityContextRepository(jwtSecurityContextRepository)
        .authorizeExchange()
        .anyExchange()
        .permitAll()
        .and()
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final var corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*"));
    corsConfig.setAllowedMethods(
        List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
    corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "Access"));
    final var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfig);
    return urlBasedCorsConfigurationSource;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder($2B, 12);
  }
}
