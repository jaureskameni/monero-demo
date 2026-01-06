package cm.klg.monero_demo.adapter.rest.outbound;

import static org.junit.jupiter.api.Assertions.*;

import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import ok.http3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class MoneroRpcClientTest {

  private MockWebServer mockWebServer;
  private MoneroRpcClient moneroRpcClient;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    String baseUrl = mockWebServer.url("/json_rpc").toString();
    RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();
    moneroRpcClient = new MoneroRpcClient(restClient);
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void createSubAddress_shouldReturnAddress_whenSuccessful() {
    // Given
    String expectedAddress = "888tL2fgwM3tMy4a1ERgNfA9Am1iF8s3p5c8fXwh3D2x4fA4b2g1f3a1e6c5b9d8a3d4e7f";
    String mockResponse =
        "{\"jsonrpc\":\"2.0\",\"id\":\"0\",\"result\":{\"address\":"
            + expectedAddress
            + ",\"address_index\":1}}";
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"));

    // When
    String actualAddress = moneroRpcClient.createSubAddress("test-label");

    // Then
    assertEquals(expectedAddress, actualAddress);
  }

  @Test
  void createSubAddress_shouldThrowMoneroRpcException_whenRpcError() {
    // Given
    String mockResponse =
        "{\"jsonrpc\":\"2.0\",\"id\":\"0\",\"error\":{\"code\":-1,\"message\":\"Some RPC error\"}}";
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"));

    // When & Then
    MoneroRpcException exception =
        assertThrows(
            MoneroRpcException.class, () -> moneroRpcClient.createSubAddress("test-label"));
    assertTrue(exception.getMessage().contains("Some RPC error"));
  }

  @Test
  void createSubAddress_shouldThrowMoneroRpcException_whenHttpStatusNot2xx() {
    // Given
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));

    // When & Then
    MoneroRpcException exception =
        assertThrows(
            MoneroRpcException.class, () -> moneroRpcClient.createSubAddress("test-label"));
    assertTrue(exception.getMessage().contains("Error communicating with Monero Wallet RPC"));
  }

  @Test
  void createSubAddress_shouldThrowMoneroRpcException_whenResponseIsEmpty() {
    // Given
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    // When & Then
    MoneroRpcException exception =
        assertThrows(
            MoneroRpcException.class, () -> moneroRpcClient.createSubAddress("test-label"));
    assertTrue(exception.getMessage().contains("Error communicating with Monero Wallet RPC"));
  }
}
