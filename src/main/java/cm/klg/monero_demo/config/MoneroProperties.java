package cm.klg.monero_demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "jkdev.monero")
public class MoneroProperties {
  private Rpc rpc = new Rpc();

  @Setter
  @Getter
  public static class Rpc {
    private String url;
    private boolean enabled = false;
    private String username;
    private String password;
    private int accountIndex = 0;
    private String version = "2.0";
    private String id = "0";
  }
}
