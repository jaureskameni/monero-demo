package cm.klg.monero_demo.adapter.config;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.config.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeans {
  @Bean
  public Configurations configurations(ApplicationProperties applicationProperties) {
    return new ApplicationsConf(applicationProperties);
  }
}
