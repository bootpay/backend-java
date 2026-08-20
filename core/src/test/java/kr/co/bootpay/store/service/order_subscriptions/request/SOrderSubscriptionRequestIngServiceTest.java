package kr.co.bootpay.store.service.order_subscriptions.request;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SOrderSubscriptionRequestIngServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
        bootpay.setTokenFromAPI("test_token");
        return bootpay;
    }

    @Test
    public void 중도해지_수수료는_GET_calculate_termination_fee로_요청한다() throws Exception {
        HttpGet get = SOrderSubscriptionRequestIngService.calculateTerminationFeeRequest(bootpay(), "order_subscription_id_value", null);

        assertEquals("GET", get.getMethod());
        assertEquals("/v1/order_subscriptions/requests/ing/calculate_termination_fee", get.getURI().getPath());
        assertEquals("order_subscription_id=order_subscription_id_value", get.getURI().getQuery());
    }

    @Test
    public void order_number만_전달하면_order_number로_조회한다() throws Exception {
        HttpGet get = SOrderSubscriptionRequestIngService.calculateTerminationFeeRequest(bootpay(), null, "order_number_value");

        assertEquals("order_number=order_number_value", get.getURI().getQuery());
    }

    @Test
    public void 둘_다_전달하면_둘_다_전송한다() throws Exception {
        String query = SOrderSubscriptionRequestIngService
                .calculateTerminationFeeRequest(bootpay(), "order_subscription_id_value", "order_number_value")
                .getURI().getQuery();

        assertTrue(query.contains("order_subscription_id=order_subscription_id_value"));
        assertTrue(query.contains("order_number=order_number_value"));
    }

    @Test
    public void 둘_다_없으면_예외를_발생시킨다() {
        assertThrows(IllegalArgumentException.class,
                () -> SOrderSubscriptionRequestIngService.calculateTerminationFeeRequest(bootpay(), null, null));
    }

    @Test
    public void 토큰이_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");

        assertThrows(IllegalArgumentException.class,
                () -> SOrderSubscriptionRequestIngService.calculateTerminationFeeRequest(bootpay, "order_subscription_id_value", null));
    }
}
