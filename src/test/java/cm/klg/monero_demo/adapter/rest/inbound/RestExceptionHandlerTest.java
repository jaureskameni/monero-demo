package cm.klg.monero_demo.adapter.rest.inbound;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest
@ContextConfiguration(classes = {RestExceptionHandler.class, RestExceptionHandlerTest.TestController.class})
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock CryptoWalletAddressController as it's a dependency of the web layer
    // but not needed for this test.
    @MockBean
    private CryptoWalletAddressController cryptoWalletAddressController;

    @RestController
    static class TestController {
        @GetMapping("/test/rpc-error")
        public void throwRpcError() {
            throw new MoneroRpcException("Monero service is down");
        }

        @GetMapping("/test/conflict-error")
        public void throwConflictError() {
            throw new ResourceAlreadyExistsException("Address already exists");
        }
    }

    @Test
    void whenMoneroRpcException_thenReturns503() throws Exception {
        mockMvc.perform(get("/test/rpc-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.title").value("Monero Service Unavailable"))
                .andExpect(jsonPath("$.detail").value("Monero service is down"));
    }

    @Test
    void whenResourceAlreadyExistsException_thenReturns409() throws Exception {
        mockMvc.perform(get("/test/conflict-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Resource Already Exists"))
                .andExpect(jsonPath("$.detail").value("Address already exists"));
    }
}
