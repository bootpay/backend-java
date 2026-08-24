package kr.co.bootpay.commerce;

import kr.co.bootpay.TestConfig;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Development-only check for the Commerce Basic token endpoint. */
@DisplayName("Commerce API - Basic authentication (development)")
class CommerceBasicAuthenticationLiveTest {

    private static BootpayStore store;

    @BeforeAll
    static void setUp() {
        TestConfig.assumeCommerceLiveAllowed();
        store = TestConfig.createBootpayStore();
    }

    @Test
    @DisplayName("Basic token request returns an access token")
    void basicTokenRequestReturnsAccessToken() throws Exception {
        BootpayStoreResponse response = store.getAccessToken();
        Map<String, Object> data = response.getData();

        assertAll(
                () -> assertTrue(response.isSuccess(), "token endpoint HTTP status=" + response.getHttpStatus()),
                () -> assertNotNull(data, "token response data must be a JSON object"),
                () -> assertTrue(data != null && data.containsKey("access_token"), "access_token is required")
        );
    }
}
