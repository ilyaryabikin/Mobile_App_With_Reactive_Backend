package se.ifmo.mobile.server.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ifmo.mobile.server.domain.User;
import se.ifmo.mobile.server.dtos.PersonDto;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PersonMapper implements Mapper<User, PersonDto> {

  @Override
  public User mapToPersistable(final PersonDto dto) {
    log.debug("Mapping dto = {} to persistable", dto);
    return User.builder()
        .id(dto.getId())
        .username(dto.getUsername())
        .password(dto.getPassword())
        .name(dto.getName())
        .surname(dto.getSurname())
        .city(dto.getCity())
        .phoneNumber(dto.getPhoneNumber())
        .build();
  }

  @Override
  public PersonDto mapToDto(final User persistable) {
    log.debug("Mapping persistable = {} to dto", persistable);
    return PersonDto.builder()
        .id(persistable.getId())
        .username(persistable.getUsername())
        .password(persistable.getPassword())
        .name(persistable.getName())
        .surname(persistable.getSurname())
        .city(persistable.getCity())
        .phoneNumber(persistable.getPhoneNumber())
        .build();
  }

  @Override
  public void update(final User persistable, final PersonDto dto) {
    log.debug("Updating persistable = {} from dto = {}", persistable, dto);
    if (dto.getName() != null) {
      persistable.setName(dto.getName());
    }
    if (dto.getSurname() != null) {
      persistable.setSurname(dto.getSurname());
    }
    if (dto.getCity() != null) {
      persistable.setCity(dto.getCity());
    }
    if (dto.getPhoneNumber() != null) {
      persistable.setPhoneNumber(dto.getPhoneNumber());
    }
  }
}
