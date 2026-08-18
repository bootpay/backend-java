package kr.co.bootpay.store.service.webhook;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.webhook.TestWebhookParams;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SWebhookServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    public void 테스트_웹훅은_POST_webhook_test로_요청한다() throws Exception {
        HttpPost post = SWebhookService.sendTestWebhookRequest(bootpay(), null);

        assertEquals("POST", post.getMethod());
        assertEquals("https://api.bootapi.com/v1/webhook/test", post.getURI().toString());
    }

    @Test
    public void header_content_type을_snake_case로_전송한다() throws Exception {
        TestWebhookParams params = new TestWebhookParams();
        params.headerContentType = "application/json";

        String body = EntityUtils.toString(SWebhookService.sendTestWebhookRequest(bootpay(), params).getEntity(), "UTF-8");

        assertTrue(body.contains("\"header_content_type\":\"application/json\""));
    }

    @Test
    public void 값이_설정되지_않으면_빈_payload로_전송한다() throws Exception {
        String body = EntityUtils.toString(SWebhookService.sendTestWebhookRequest(bootpay(), new TestWebhookParams()).getEntity(), "UTF-8");

        assertEquals("{}", body);
        assertFalse(body.contains("header_content_type"));
    }

    @Test
    public void 인증정보가_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        assertThrows(Exception.class, () -> SWebhookService.sendTestWebhookRequest(bootpay, new TestWebhookParams()));
    }
}
