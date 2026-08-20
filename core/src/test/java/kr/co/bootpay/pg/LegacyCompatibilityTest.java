package kr.co.bootpay.pg;

import kr.co.bootpay.pg.Bootpay;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Legacy Compatibility")
class LegacyCompatibilityTest {
    static class TestBootpay extends Bootpay {
        void applyAuthHeader(HttpGet request) {
            setAuthHeader(request);
        }

        String tokenValue() {
            return getTokenValue();
        }

        String basicAuthValue() {
            return getBasicAuthValue();
        }
    }

    @Test
    @DisplayName("application_id/private_key 생성자는 계속 동작해야 한다")
    void legacyConstructorStillWorks() {
        Bootpay bootpay = new Bootpay("legacy_application_id", "legacy_private_key", "DEVELOPMENT");

        assertEquals("legacy_application_id", bootpay.application_id);
        assertEquals("legacy_private_key", bootpay.private_key);
        assertNull(bootpay.client_key);
        assertNull(bootpay.secret_key);
        assertEquals(bootpay.DEVELOPMENT, bootpay.baseUrl);
    }

    @Test
    @DisplayName("legacy token은 Bearer Authorization으로 유지되어야 한다")
    void legacyTokenUsesBearerAuthorization() {
        TestBootpay bootpay = new TestBootpay();
        bootpay.application_id = "legacy_application_id";
        bootpay.private_key = "legacy_private_key";
        bootpay.setToken("legacy_access_token");

        HttpGet get = new HttpGet("https://api.bootpay.co.kr/v2/receipt/test");
        bootpay.applyAuthHeader(get);

        assertEquals("Bearer legacy_access_token", get.getFirstHeader("Authorization").getValue());
        assertEquals("Bearer legacy_access_token", bootpay.tokenValue());
    }

    @Test
    @DisplayName("client_key/secret_key는 Basic Authorization을 사용해야 한다")
    void clientKeyUsesBasicAuthorization() {
        TestBootpay bootpay = new TestBootpay();
        bootpay.client_key = "ck";
        bootpay.secret_key = "sk";

        HttpGet get = new HttpGet("https://api.bootpay.co.kr/v2/receipt/test");
        bootpay.applyAuthHeader(get);

        assertEquals("Basic Y2s6c2s=", get.getFirstHeader("Authorization").getValue());
        assertEquals("Basic Y2s6c2s=", bootpay.basicAuthValue());
    }

    @Test
    @DisplayName("access_token이 문자열이 아니면 token을 설정하지 않아야 한다")
    void nonStringAccessTokenDoesNotOverwriteToken() throws Exception {
        TestBootpay bootpay = new TestBootpay() {
            @Override
            public HashMap<String, Object> doPostWithoutAuth(String url, Object payload) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("access_token", 123);
                return result;
            }
        };
        bootpay.application_id = "legacy_application_id";
        bootpay.private_key = "legacy_private_key";
        bootpay.setToken("stale_access_token");

        HashMap<String, Object> result = kr.co.bootpay.pg.service.TokenService.getAccessToken(bootpay);

        assertEquals(123, result.get("access_token"));
        assertNull(bootpay.token);
    }
}
