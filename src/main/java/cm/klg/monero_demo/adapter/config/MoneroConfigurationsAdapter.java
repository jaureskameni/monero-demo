package cm.klg.monero_demo.adapter.config;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.config.MoneroProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MoneroConfigurationsAdapter implements Configurations {
  private final MoneroProperties moneroProperties;

  @Override
  public String url() {
    return moneroProperties.getRpc().getUrl();
  }

  @Override
  public String username() {
    return moneroProperties.getRpc().getUsername();
  }

  @Override
  public String password() {
    return moneroProperties.getRpc().getPassword();
  }

  @Override
  public boolean enabled() {
    return moneroProperties.getRpc().isEnabled();
  }

  @Override
  public int accountIndex() {
    return moneroProperties.getRpc().getAccountIndex();
  }

  @Override
  public String rpcVersion() {
    return moneroProperties.getRpc().getVersion();
  }

  @Override
  public String rpcId() {
    return moneroProperties.getRpc().getId();
  }
}
