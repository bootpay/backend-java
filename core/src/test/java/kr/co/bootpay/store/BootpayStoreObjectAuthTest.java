package kr.co.bootpay.store;

import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BootpayStoreObjectAuthTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private static final String BASIC_VALUE = Base64.getEncoder()
            .encodeToString((CLIENT_KEY + ":" + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    public void basicAuthentification은_토큰을_저장하지_않는다() {
        BootpayStoreObject bootpay = bootpay();

        assertEquals(BASIC_VALUE, bootpay.basicAuthentification());
        assertNull(bootpay.getToken());
    }

    @Test
    public void 토큰이_없으면_Basic_인증을_사용한다() throws Exception {
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
    public void Basic_인증은_반복_요청에도_유지된다() throws Exception {
        BootpayStoreObject bootpay = bootpay();

        bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));
        HttpGet get = bootpay.httpGet("products");

        // basic 인증 값이 토큰으로 저장되면 두번째 요청부터 Bearer 로 전송되는 버그가 있었다
        assertNull(bootpay.getToken());
        assertEquals("Basic " + BASIC_VALUE, get.getFirstHeader("Authorization").getValue());
    }

    @Test
    public void 토큰이_있으면_Bearer_인증을_사용한다() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));

        assertEquals("Bearer access_token_value", get.getFirstHeader("Authorization").getValue());
        assertEquals("Bearer access_token_value", post.getFirstHeader("Authorization").getValue());
    }

    @Test
    public void RequestContext의_토큰이_우선한다() throws Exception {
        BootpayStoreObject bootpay = bootpay();
        bootpay.setTokenFromAPI("access_token_value");
        RequestContext context = RequestContext.builder().role("manager").token("context_token").build();

        HttpGet get = bootpay.httpGet("products", context);

        assertEquals("Bearer context_token", get.getFirstHeader("Authorization").getValue());
        assertEquals("manager", get.getFirstHeader("BOOTPAY-ROLE").getValue());
    }

    @Test
    public void 키와_토큰이_모두_없으면_Authorization_헤더를_설정하지_않는다() throws Exception {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        HttpGet get = bootpay.httpGet("products");
        HttpPost post = bootpay.httpPost("products", new StringEntity("{}", "UTF-8"));

        assertNull(bootpay.basicAuthentification());
        assertNull(get.getFirstHeader("Authorization"));
        assertNull(post.getFirstHeader("Authorization"));
    }

    @Test
    public void requestAccessToken은_기존_동작을_유지한다() {
        assertEquals("Basic " + BASIC_VALUE, bootpay().requestAccessToken());
        assertEquals("", new BootpayStoreObject(new TokenPayload(), "PRODUCTION").requestAccessToken());
    }
}
