package kr.co.bootpay.store;

import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Commerce Basic-only 인증의 v3.3.0 회귀 기록.
 *
 * @deprecated 3.4.0부터 {@link BootpayStoreObjectAuthorizationContractTest}가 현재 Basic-only
 * 계약을 검증한다. 이 클래스는 v3.3.0의 회귀 기록이며 실행하지 않는다.
 */
@Deprecated
@Disabled("Deprecated v3.3.0 Basic-only authorization contract; replaced by BootpayStoreObjectAuthorizationContractTest")
@DisplayName("Commerce API - 인증 헤더")
class BootpayStoreObjectAuthTest {

    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private static final String BASIC_VALUE = "Basic " + Base64.getEncoder()
            .encodeToString((CLIENT_KEY + ":" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    @DisplayName("Basic 인증 값 계산은 토큰을 저장하지 않는다")
    void basicAuthDoesNotStoreToken() {
        BootpayStoreObject bootpay = bootpay();

        assertEquals(BASIC_VALUE, bootpay.requestAccessToken());
        assertNull(bootpay.getToken());
    }

    @Test
    @DisplayName("토큰이 없으면 GET/POST/PUT/DELETE 모두 Basic 인증을 쓴다")
    void allVerbsUseBasicWithoutToken() throws Exception {
        BootpayStoreObject bootpay = bootpay();

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpPut put = bootpay.httpPut("products/1", new StringEntity("{}", "UTF-8"));
        HttpDelete delete = bootpay.httpDelete("products/1");

        assertEquals(BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, post.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, put.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, delete.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("Basic 인증은 반복 요청에도 유지된다 (토큰으로 저장되면 안 된다)")
    void basicAuthSurvivesRepeatedRequests() throws Exception {
        BootpayStoreObject bootpay = bootpay();

        bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpGet get = bootpay.httpGet("products");

        // basic 인증 값이 토큰으로 저장되면 두번째 요청부터 Bearer 로 전송되는 버그가 있었다
        assertNull(bootpay.getToken());
        assertEquals(BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("토큰이 발급되어 있어도 Commerce 인증은 Basic 을 쓴다")
    void issuedTokenDoesNotSwitchToBearer() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpPut put = bootpay.httpPut("products/1", new StringEntity("{}", "UTF-8"));
        HttpDelete delete = bootpay.httpDelete("products/1");

        // Bearer 로 전환하면 토큰 30분 만료 후 복구 수단이 없다. 만료 파싱·재발급·401 폴백을
        // 갖추기 전까지는 Go / Python / PHP / .NET 과 같이 Basic 을 유지한다.
        assertEquals(BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, post.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, put.getFirstHeader("Authorization").getValue());
        assertEquals(BASIC_VALUE, delete.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("RequestContext 의 토큰은 인증에 쓰이지 않고 role 만 적용된다")
    void contextTokenDoesNotAffectAuthorization() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");
        RequestContext context = RequestContext.builder().role("manager").token("context_token").build();

        HttpGet get = bootpay.httpGet("products", context);

        assertEquals(BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
        assertEquals("manager", get.getFirstHeader("BOOTPAY-ROLE").getValue());
    }

    @Test
    @DisplayName("키가 없으면 Authorization 값이 비어 있다 (v3.2.0 과 동일)")
    void noCredentialsKeepsLegacyEmptyHeader() throws Exception {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));

        assertEquals("", get.getFirstHeader("Authorization").getValue());
        assertEquals("", post.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("requestAccessToken 은 기존 동작(Basic 값 계산)을 유지한다")
    void requestAccessTokenKeepsLegacyBehaviour() {
        assertEquals(BASIC_VALUE, bootpay().requestAccessToken());
        assertEquals("", new BootpayStoreObject(new TokenPayload(), "PRODUCTION").requestAccessToken());
    }
}
