package kr.co.bootpay.store;

import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Commerce Basic-only authorization contract.
 *
 * <p>The SDK supports Java 8, so this uses the available JUnit Jupiter 5 nested-test style rather
 * than newer Java language features.</p>
 */
@DisplayName("Commerce API - Authorization contract")
class BootpayStoreObjectAuthorizationContractTest {

    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";
    private static final String BASIC_VALUE = "Basic " + Base64.getEncoder()
            .encodeToString((CLIENT_KEY + ":" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

    private BootpayStoreObject store() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Nested
    @DisplayName("client_key and secret_key are configured")
    class BasicAuthorization {

        @Test
        @DisplayName("stored tokens do not replace Basic authorization")
        void storedTokenDoesNotReplaceBasicCredentials() throws Exception {
            BootpayStoreObject store = store();
            store.setTokenFromAPI("issued-token");

            HttpGet request = store.httpGet("products");

            assertEquals(BASIC_VALUE, request.getFirstHeader("Authorization").getValue());
        }

        @Test
        @DisplayName("request context token does not replace Basic authorization")
        void requestContextTokenDoesNotReplaceBasicCredentials() throws Exception {
            BootpayStoreObject store = store();
            store.setTokenFromAPI("issued-token");
            RequestContext context = RequestContext.builder().token("request-token").build();

            HttpGet request = store.httpGet("products", context);

            assertEquals(BASIC_VALUE, request.getFirstHeader("Authorization").getValue());
        }
    }

    @Nested
    @DisplayName("credentials are incomplete")
    class CredentialValidation {

        @Test
        @DisplayName("client credentials use Basic authorization")
        void clientCredentialsUseBasicAuthorization() throws Exception {
            HttpGet request = store().httpGet("products");

            assertEquals(BASIC_VALUE, request.getFirstHeader("Authorization").getValue());
        }

        @Test
        @DisplayName("missing credentials omit the Authorization header")
        void missingCredentialsOmitAuthorizationHeader() throws Exception {
            BootpayStoreObject store = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");
            HttpGet request = store.httpGet("products");

            assertAll(
                    () -> assertEquals("", store.requestAccessToken()),
                    () -> assertNull(request.getFirstHeader("Authorization")),
                    () -> assertThrows(IllegalStateException.class, store::requireCommerceCredentials)
            );
        }

        @Test
        @DisplayName("partial client_key/secret_key is rejected before dispatch")
        void partialCredentialsAreRejected() {
            BootpayStoreObject clientOnly = new BootpayStoreObject(new TokenPayload("client", null), "PRODUCTION");
            BootpayStoreObject secretOnly = new BootpayStoreObject(new TokenPayload(null, "secret"), "PRODUCTION");

            assertAll(
                    () -> assertThrows(IllegalStateException.class, clientOnly::requireCommerceCredentials),
                    () -> assertThrows(IllegalStateException.class, secretOnly::requireCommerceCredentials)
            );
        }
    }
}
