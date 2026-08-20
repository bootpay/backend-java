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
 * Commerce 인증 헤더 회귀 검증.
 *
 * <p>2-x-development 브랜치에 있던 검증을 현행 동작 기준으로 옮긴 것이다.</p>
 */
@DisplayName("Commerce API - 인증 헤더")
class BootpayStoreObjectAuthTest {

    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private static final String BASIC_VALUE = Base64.getEncoder()
            .encodeToString((CLIENT_KEY + ":" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    @DisplayName("Basic 인증 값 계산은 토큰을 저장하지 않는다")
    void basicAuthDoesNotStoreToken() {
        BootpayStoreObject bootpay = bootpay();

        assertEquals("Basic " + BASIC_VALUE, bootpay.requestAccessToken());
        assertNull(bootpay.getToken());
    }

    @Test
    @DisplayName("GET/POST/PUT/DELETE 모두 Basic 인증 헤더를 붙인다")
    void allVerbsCarryBasicAuth() throws Exception {
        BootpayStoreObject bootpay = bootpay();

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpPut put = bootpay.httpPut("products/1", new StringEntity("{}", "UTF-8"));
        HttpDelete delete = bootpay.httpDelete("products/1");

        assertEquals("Basic " + BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
        assertEquals("Basic " + BASIC_VALUE, post.getFirstHeader("Authorization").getValue());
        assertEquals("Basic " + BASIC_VALUE, put.getFirstHeader("Authorization").getValue());
        assertEquals("Basic " + BASIC_VALUE, delete.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("Basic 인증은 반복 요청에도 유지된다 (토큰으로 저장되면 안 된다)")
    void basicAuthSurvivesRepeatedRequests() throws Exception {
        BootpayStoreObject bootpay = bootpay();

        bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpGet get = bootpay.httpGet("products");

        // basic 인증 값이 토큰으로 저장되면 두번째 요청부터 Bearer 로 전송되는 버그가 있었다
        assertNull(bootpay.getToken());
        assertEquals("Basic " + BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("RequestContext 의 role 은 인스턴스 기본 role 을 덮는다")
    void contextRoleOverridesInstanceRole() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        RequestContext context = RequestContext.builder().role("manager").build();

        HttpGet get = bootpay.httpGet("products", context);

        assertEquals("manager", get.getFirstHeader("BOOTPAY-ROLE").getValue());
        assertEquals("Basic " + BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
    }

    @Test
    @DisplayName("키가 없으면 Basic 값이 빈 문자열이다")
    void noKeysYieldEmptyAuthValue() {
        assertEquals("", new BootpayStoreObject(new TokenPayload(), "PRODUCTION").requestAccessToken());
    }
}
