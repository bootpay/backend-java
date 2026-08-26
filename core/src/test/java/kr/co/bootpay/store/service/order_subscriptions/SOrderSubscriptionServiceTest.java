package kr.co.bootpay.store.service.order_subscriptions;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SOrderSubscriptionServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    public void 구독목록은_GET_order_subscriptions로_요청한다() throws Exception {
        HttpGet get = SOrderSubscriptionService.listRequest(bootpay(), null);

        assertEquals("GET", get.getMethod());
        assertEquals("https://api.bootapi.com/v1/order_subscriptions", get.getURI().toString());
    }

    @Test
    public void page와_limit을_함께_전송한다() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.page = 2;
        params.limit = 50;

        String query = SOrderSubscriptionService.listRequest(bootpay(), params).getURI().getQuery();

        assertTrue(query.contains("page=2"));
        assertTrue(query.contains("limit=50"));
    }

    @Test
    public void request_type과_status를_각자의_값으로_전송한다() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.requestType = 3;
        params.status = 1;
        params.eAt = "2026-08-14";

        String query = SOrderSubscriptionService.listRequest(bootpay(), params).getURI().getQuery();

        assertTrue(query.contains("request_type=3"));
        assertTrue(query.contains("status=1"));
        assertTrue(query.contains("e_at=2026-08-14"));
    }

    @Test
    public void 값이_설정되지_않은_필드는_전송하지_않는다() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.userId = "user_id_value";

        String query = SOrderSubscriptionService.listRequest(bootpay(), params).getURI().getQuery();

        assertEquals("user_id=user_id_value", query);
        assertFalse(query.contains("limit"));
        assertFalse(query.contains("status"));
    }

    @Test
    public void 주문번호로_구독을_역조회한다() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.orderNumber = "ORD-20260826-001";

        String query = SOrderSubscriptionService.listRequest(bootpay(), params).getURI().getQuery();

        assertEquals("order_number=ORD-20260826-001", query);
    }

    @Test
    public void ck_sk만으로_토큰없이_기본인증을_구성한다() throws Exception {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
        HttpGet request = SOrderSubscriptionService.listRequest(bootpay, new OrderSubscriptionListParams());

        assertEquals("Basic dGVzdF9jbGllbnRfa2V5OnRlc3Rfc2VjcmV0X2tleQ==",
                request.getFirstHeader("Authorization").getValue());
    }
}
