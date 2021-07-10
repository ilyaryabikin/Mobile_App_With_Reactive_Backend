package se.ifmo.mobile.server.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

  @Bean
  public LocalValidatorFactoryBean localValidatorFactoryBean() {
    return new LocalValidatorFactoryBean();
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener(
      final LocalValidatorFactoryBean localValidatorFactoryBean) {
    return new ValidatingMongoEventListener(localValidatorFactoryBean);
  }
}
