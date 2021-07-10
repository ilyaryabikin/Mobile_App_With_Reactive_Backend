package se.ifmo.mobile.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MobileServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MobileServerApplication.class, args);
  }
}
