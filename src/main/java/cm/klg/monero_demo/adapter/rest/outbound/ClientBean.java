package cm.klg.monero_demo.adapter.rest.outbound;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class ClientBean {
  private final Configurations configurations;

  @Bean
  public RestClient restClient() {
    return RestClient.builder().baseUrl(configurations.url()).build();
  }

  @Bean
  public MoneroWalletClient moneroWalletPort(RestClient restClient) {
    return new MoneroRpcClient(restClient);
  }
}
