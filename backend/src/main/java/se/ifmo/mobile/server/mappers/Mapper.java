package se.ifmo.mobile.server.mappers;

import org.springframework.data.domain.Persistable;
import se.ifmo.mobile.server.dtos.Dto;

public interface Mapper<T extends Persistable<?>, V extends Dto> {

  T mapToPersistable(final V dto);

  V mapToDto(final T persistable);

  void update(final T persistable, final V dto);
}
