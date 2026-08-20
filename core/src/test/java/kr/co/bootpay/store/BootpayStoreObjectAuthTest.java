package kr.co.bootpay.store;

import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Commerce 인증 헤더 규칙 검증.
 *
 * <p>기준 SDK(NodeJS) 및 Ruby / Go / Python / PHP / .NET 과 동일하게
 * <b>토큰이 있으면 Bearer, 없으면 client_key/secret_key Basic, 둘 다 없으면 헤더 미부착</b> 이다.</p>
 */
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
        assertEquals(BASIC_VALUE, bootpay.authorizationHeader());
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
    @DisplayName("토큰이 발급되어 있으면 Bearer 인증을 쓴다")
    void tokenTakesPrecedenceOverBasic() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpPut put = bootpay.httpPut("products/1", new StringEntity("{}", "UTF-8"));
        HttpDelete delete = bootpay.httpDelete("products/1");

        assertEquals("Bearer access_token_value", get.getFirstHeader("Authorization").getValue());
        assertEquals("Bearer access_token_value", post.getFirstHeader("Authorization").getValue());
        assertEquals("Bearer access_token_value", put.getFirstHeader("Authorization").getValue());
        assertEquals("Bearer access_token_value", delete.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("RequestContext 의 토큰이 인스턴스 토큰보다 우선한다")
    void contextTokenOverridesInstanceToken() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");
        RequestContext context = RequestContext.builder().role("manager").token("context_token").build();

        HttpGet get = bootpay.httpGet("products", context);

        assertEquals("Bearer context_token", get.getFirstHeader("Authorization").getValue());
        assertEquals("manager", get.getFirstHeader("BOOTPAY-ROLE").getValue());
    }

    @Test
    @DisplayName("키와 토큰이 모두 없으면 Authorization 헤더를 붙이지 않는다")
    void noCredentialsMeansNoAuthorizationHeader() throws Exception {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));

        assertNull(bootpay.authorizationHeader());
        assertNull(get.getFirstHeader("Authorization"), "빈 Authorization 헤더를 보내면 안 된다");
        assertNull(post.getFirstHeader("Authorization"), "빈 Authorization 헤더를 보내면 안 된다");
    }

    @Test
    @DisplayName("requestAccessToken 은 기존 동작(Basic 값 계산)을 유지한다")
    void requestAccessTokenKeepsLegacyBehaviour() {
        assertEquals(BASIC_VALUE, bootpay().requestAccessToken());
        assertEquals("", new BootpayStoreObject(new TokenPayload(), "PRODUCTION").requestAccessToken());
    }
}
