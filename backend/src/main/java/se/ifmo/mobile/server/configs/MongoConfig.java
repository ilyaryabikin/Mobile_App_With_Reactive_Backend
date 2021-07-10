package se.ifmo.mobile.server.configs;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import se.ifmo.mobile.server.converters.StringToGrantedAuthorityConverter;

@Configuration
public class MongoConfig {

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    final var converters = new ArrayList<Converter<?, ?>>();
    converters.add(new StringToGrantedAuthorityConverter());
    return new MongoCustomConversions(converters);
  }
}
