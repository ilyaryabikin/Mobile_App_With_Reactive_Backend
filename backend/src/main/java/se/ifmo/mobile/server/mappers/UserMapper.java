package se.ifmo.mobile.server.mappers;

import java.util.Collections;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.UserDto;

@Component
@Slf4j
public class UserMapper implements Mapper<User, UserDto> {

  @Override
  public User mapToPersistable(final UserDto dto) {
    log.debug("Mapping dto = {} to persistable", dto);
    return User.builder()
        .id(dto.getId())
        .username(dto.getUsername())
        .password(dto.getPassword())
        .build();
  }

  @Override
  public UserDto mapToDto(final User persistable) {
    log.debug("Mapping persistable = {} to dto", persistable);
    return UserDto.builder()
        .id(persistable.getId())
        .username(persistable.getUsername())
        .password(persistable.getPassword())
        .authorities(
            persistable.getAuthorities().isEmpty()
                ? Collections.emptyList()
                : persistable.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
        .build();
  }

  @Override
  public void update(final User persistable, final UserDto dto) {
    log.debug("Updating persistable = {} from dto = {}", persistable, dto);
    if (dto.getUsername() != null) {
      persistable.setUsername(dto.getUsername());
    }
    if (dto.getPassword() != null) {
      persistable.setPassword(dto.getPassword());
    }
  }
}
