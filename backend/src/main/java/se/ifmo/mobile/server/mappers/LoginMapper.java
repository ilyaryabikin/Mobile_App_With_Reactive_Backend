package se.ifmo.mobile.server.mappers;

import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.LoginDto;

@Component
@Slf4j
public class LoginMapper implements Mapper<User, LoginDto> {

  @Override
  public User mapToPersistable(final LoginDto dto) {
    log.debug("Mapping dto = {} to persistable", dto);
    return User.builder().username(dto.getUsername()).password(dto.getPassword()).build();
  }

  @Override
  public LoginDto mapToDto(final User persistable) {
    log.debug("Mapping persistable = {} to dto", persistable);
    return LoginDto.builder()
        .id(persistable.getId())
        .authorities(
            persistable.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .build();
  }

  public LoginDto mapToDto(final User persistable, final String token, final Date expiresAt) {
    final var dto = mapToDto(persistable);
    dto.setToken(token);
    dto.setExpiresAt(expiresAt);
    return dto;
  }

  @Override
  public void update(final User persistable, final LoginDto dto) {
    log.warn("Can't update persistable = {} from dto = {}", persistable, dto);
    throw new UnsupportedOperationException("Can not Update user from LoginDto");
  }
}
