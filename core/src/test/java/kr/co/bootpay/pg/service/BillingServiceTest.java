package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BillingServiceTest {

    private BootpayObject bootpay() {
        BootpayObject bootpay = new BootpayObject("test_application_id", "test_private_key", "PRODUCTION");
        bootpay.token = "test_access_token";
        return bootpay;
    }

    @Test
    public void 우선순위_빌링키_조회는_widget_key와_user_id를_쿼리로_전송한다() throws Exception {
        List<NameValuePair> params = BillingService.sequentialBillingKeyParams("widget_key_value", "user_id_value");
        String query = bootpay().httpGet("subscribe/sequential_billing_key/billing_key_value", params).getURI().getQuery();

        assertTrue(query.contains("widget_key=widget_key_value"));
        assertTrue(query.contains("user_id=user_id_value"));
    }

    @Test
    public void user_id가_없으면_widget_key만_전송한다() throws Exception {
        List<NameValuePair> params = BillingService.sequentialBillingKeyParams("widget_key_value", null);
        HttpGet get = bootpay().httpGet("subscribe/sequential_billing_key/billing_key_value", params);

        assertEquals("https://api.bootpay.co.kr/v2/subscribe/sequential_billing_key/billing_key_value?widget_key=widget_key_value",
                get.getURI().toString());
        assertFalse(get.getURI().toString().contains("user_id"));
    }

    @Test
    public void 필수값이_없으면_예외를_발생시킨다() {
        assertThrows(Exception.class, () -> BillingService.lookupSequentialBillingKey(bootpay(), null, "billing_key_value", "user_id_value"));
        assertThrows(Exception.class, () -> BillingService.lookupSequentialBillingKey(bootpay(), "widget_key_value", null, "user_id_value"));
    }
}
