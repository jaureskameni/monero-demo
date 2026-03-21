package cm.klg.monero_demo.adapter.rest.outbound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressId;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class MoneroRpcClientTest {

  private MockWebServer mockWebServer;
  private MoneroRpcClient moneroRpcClient;
  private ObjectMapper objectMapper;

  @Mock private Configurations configurations;

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();

    String baseUrl = mockWebServer.url("/json_rpc").toString();

    when(configurations.url()).thenReturn(baseUrl);
    when(configurations.rpcVersion()).thenReturn("2.0");
    when(configurations.rpcId()).thenReturn("0");
    when(configurations.accountIndex()).thenReturn(0);

    moneroRpcClient = new MoneroRpcClient(restTemplate, configurations, objectMapper);
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void createSubAddress_shouldReturnAddress_whenSuccessful() {
    // Given
    String expectedAddress =
        "888tL2fgwM3tMy4a1ERgNfA9Am1iF8s3p5c8fXwh3D2x4fA4b2g1f3a1e6c5b9d8a3d4e7f";
    String mockResponse =
        "{\"jsonrpc\":\"2.0\",\"id\":\"0\",\"result\":{\"address\":\""
            + expectedAddress
            + "\",\"address_index\":1}}";
    mockWebServer.enqueue(
        new MockResponse().setBody(mockResponse).addHeader("Content-Type", "application/json"));

    // When
    String actualAddress =
        moneroRpcClient.createSubAddress(new CryptoWalletAddressId(UUID.randomUUID()));

    // Then
    assertEquals(expectedAddress, actualAddress);
  }

  @Test
  void createSubAddress_shouldThrowMoneroRpcException_whenRpcError() {
    // Given
    String mockResponse =
        "{\"jsonrpc\":\"2.0\",\"id\":\"0\",\"error\":{\"code\":-1,\"message\":\"Some RPC error\"}}";
    mockWebServer.enqueue(
        new MockResponse().setBody(mockResponse).addHeader("Content-Type", "application/json"));

    // When & Then
    MoneroRpcException exception =
        assertThrows(
            MoneroRpcException.class,
            () -> moneroRpcClient.createSubAddress(new CryptoWalletAddressId(UUID.randomUUID())));
    assertTrue(exception.getMessage().contains("Some RPC error"));
  }
}
