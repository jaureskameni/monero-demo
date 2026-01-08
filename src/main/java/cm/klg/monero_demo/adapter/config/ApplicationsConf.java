package cm.klg.monero_demo.adapter.config;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ApplicationsConf implements Configurations {
  private final ApplicationProperties applicationProperties;

  @Override
  public String url() {
    return applicationProperties.getRpc().getUrl();
  }

  @Override
  public String username() {
    return applicationProperties.getRpc().getUsername();
  }

  @Override
  public String password() {
    return applicationProperties.getRpc().getPassword();
  }
}
