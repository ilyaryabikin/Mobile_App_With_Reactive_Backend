package se.ifmo.mobile.server.converters;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class StringToGrantedAuthorityConverter implements Converter<String, GrantedAuthority> {

  @Override
  public GrantedAuthority convert(final @NotNull String source) {
    log.debug("Converted authority={} to SimpleGrantedAuthority", source);
    return new SimpleGrantedAuthority(source);
  }
}
