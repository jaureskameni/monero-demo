package cm.klg.monero_demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "monero")
public class ApplicationProperties {
  private RpcProperties rpc = new RpcProperties();

  @Setter
  @Getter
  public static class RpcProperties {
    private String url;
    private String username;
    private String password;
  }
}
