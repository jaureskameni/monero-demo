package cm.klg.monero_demo;

import cm.klg.monero_demo.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {ApplicationProperties.class})
@SpringBootApplication
public class MoneroDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoneroDemoApplication.class, args);
  }
}
