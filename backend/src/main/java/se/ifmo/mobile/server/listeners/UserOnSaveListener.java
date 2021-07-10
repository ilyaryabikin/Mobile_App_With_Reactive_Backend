package se.ifmo.mobile.server.listeners;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.ifmo.mobile.server.domain.User;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserOnSaveListener extends AbstractMongoEventListener<User> {

  private final PasswordEncoder passwordEncoder;

  @Override
  public void onBeforeSave(final BeforeSaveEvent<User> event) {
    final var user = event.getSource();
    final var document = event.getDocument();
    if (user.isNew() && document != null && !document.isEmpty()) {
      log.info(
          "Encoding user password and set default role on beforeSave event for user={}",
          user.getUsername());
      document.put("authorities", List.of("ROLE_USER"));
      document.replace("password", passwordEncoder.encode(user.getPassword()));
    }
  }
}
